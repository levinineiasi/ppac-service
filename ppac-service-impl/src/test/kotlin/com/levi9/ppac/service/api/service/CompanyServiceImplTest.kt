import com.levi9.ppac.service.api.service.CompanyService
import com.levi9.ppac.service.api.service.CompanyServiceImpl
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.domain.TrainerEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.repository.TrainerRepository
import com.levi9.ppac.service.api.security.SecurityContext
import com.levi9.ppac.service.api.service.config.TestConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.HttpStatus
import org.springframework.test.context.BootstrapWith
import org.springframework.test.context.TestPropertySource
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@SpringBootTest(
    classes = [
        CompanyServiceImpl::class
    ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@Import(TestConfig::class)
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
@TestPropertySource(properties = ["feature.mvp=true"])
class CompanyServiceImplTest {

    @Autowired
    lateinit var companyService: CompanyService<Company, UUID, Opening>

    @Autowired
    lateinit var trainerRepository: TrainerRepository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var codeRepository: CodeRepository

    @Autowired
    lateinit var openingRepository: OpeningRepository

    @Autowired
    lateinit var companyCodeRepository: CompanyCodeRepository

    @Autowired
    lateinit var securityContext: SecurityContext<Int>

    companion object {
        private val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456).apply { type = CodeType.COMPANY_CODE }
        private val trainer = TrainerEntity(UUID.randomUUID(), "Trainer1", "Some description for trainer 1")
        private val opening = OpeningEntity(
            id = UUID.randomUUID(),
            keyWords = emptyList(),
            customKeyWords = emptyList(),
            hasTechnicalInterview = false,
            hasTechnicalTest = false,
            periodCount = 4,
            periodType = PeriodType.MONTHS,
            openPositions = 20,
            acceptOnClosingOpportunity = true,
            signAgreement = false,
            trainers = listOf(trainer),
            available = true
        )
        private val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(opening) }
        private val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
        private val openingAvailable = OpeningEntity(
            id = UUID.randomUUID(),
            keyWords = emptyList(),
            customKeyWords = emptyList(),
            hasTechnicalInterview = true,
            hasTechnicalTest = true,
            periodCount = 4,
            periodType = PeriodType.MONTHS,
            openPositions = 20,
            acceptOnClosingOpportunity = true,
            signAgreement = false,
            trainers = listOf(trainer),
            available = true
        )

        private val openingUnavailable = OpeningEntity(
            id = UUID.randomUUID(),
            keyWords = emptyList(),
            customKeyWords = emptyList(),
            hasTechnicalInterview = true,
            hasTechnicalTest = true,
            periodCount = 4,
            periodType = PeriodType.MONTHS,
            openPositions = 20,
            acceptOnClosingOpportunity = true,
            signAgreement = false,
            trainers = listOf(trainer),
            available = false
        )

        private val codeEntityForCompany2 = AccessCodeEntity(UUID.randomUUID(), 112233).apply { type = CodeType.COMPANY_CODE }
        private val company2 = CompanyEntity(UUID.randomUUID(), "Amazon")
        private val companyCodeEntity2 = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany2, company2)

        private val codeEntityForCompany3 = AccessCodeEntity(UUID.randomUUID(), 223344).apply { type = CodeType.COMPANY_CODE }
        private val company3 = CompanyEntity(UUID.randomUUID(), "Endava")
        private val companyCodeEntity3 = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany3, company3)
    }

    @Test
    fun `when add opening, no access code for company given by id`() {

        insertCompanyInDbForOpening()

        val exception = assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    fun `when add opening, wrong access code for company given by id`() {

        insertCompanyInDbForOpening()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    fun `when add opening is successful`() {

        insertCompanyInDbForOpening()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val addOpening = companyService.addOpening(
            company.id,
            Opening.parse(opening)
        )

        assertNotNull(openingRepository.findById(addOpening.id))

        val updatedCompany = companyRepository.findById(company.id)
        assertTrue(updatedCompany.get().openings.map { it.id }.contains(addOpening.id))
        assertNotNull(trainerRepository.findById(trainer.id))
    }

    @Test
    fun `when add opening but trainer is from another company`() {

        insertCompanyInDbForOpening()

        val secondAccessCode = AccessCodeEntity(UUID.randomUUID(), 255255)
        val secondCompany = CompanyEntity(UUID.randomUUID(), "Google")
            .apply { openings = emptyList() }
        val openingWithSameTrainer = opening.copy().apply { id = UUID.randomUUID() }

        val secondCompanyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), secondAccessCode, secondCompany)

        securityContext.setAccessCode(secondCompanyCodeEntity.accessCode.value)

        codeRepository.save(secondAccessCode)
        companyRepository.save(secondCompany)
        companyCodeRepository.save(secondCompanyCodeEntity)

        val exception = assertThrows<ResponseStatusException> {
            companyService.addOpening(
                secondCompany.id,
                Opening.parse(openingWithSameTrainer)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `when there are companies in db findAll returns all companies`() {

        insert2CompaniesInDb()

        val result = companyService.findAll()

        assertEquals(2, result.size)

        assertEquals(Company.parse(company2) , result[0])
        assertEquals(Company.parse(company3) , result[1])
    }

    @Test
    fun `when there are no companies in db findAll returns no companies`() {

        val result = companyService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `when there is a company with a given id and no openings in db findById with that id and onlyAvailableOpenings true returns that company`() {

        insertCompanyInDb()

        val result = companyService.findById(company2.id, true)

        assertEquals(Company.parse(company2), result)
    }

    @Test
    fun `when there isn't a company with a given id in db findById with that id and onlyAvailableOpenings true returns NOT_FOUND`() {

        assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java)
        { companyService.findById(company2.id, true) }

    }

    @Test
    fun `when there is a company with a given id and openings in db findById with that id and onlyAvailableOpenings true returns that company only with the available openings`() {

        insertCompanyInDb()

        openingRepository.save(openingAvailable)
        openingRepository.save(openingUnavailable)

        val companyToSave = company2.copy().apply { openings = listOf(openingAvailable, openingUnavailable) }

        companyRepository.save(companyToSave)

        val result = companyService.findById(company2.id, true)

        val expected = Company.parse(company2.copy().apply { openings = listOf(openingAvailable) })

        assertEquals(expected, result)
    }

    @Test
    fun `when there is a company with a given id and openings in db findById with that id and onlyAvailableOpenings false returns that company with all openings`() {

        insertCompanyInDb()

        openingRepository.save(openingAvailable)
        openingRepository.save(openingUnavailable)

        val companyToSave = company2.copy().apply { openings = listOf(openingAvailable, openingUnavailable) }

        companyRepository.save(companyToSave)

        val result = companyService.findById(company2.id, false)

        val expected = Company.parse(company2.copy().apply { openings = listOf(openingAvailable, openingUnavailable) })

        assertEquals(expected, result)
    }

    @Test
    fun `when there is not a company with a given id in db updateById with that id returns 404 NotFound`() {

        insertCompanyInDb()

        assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java)
        { companyService.updateById(company3.id, Company.parse(company3)) }
    }

    @Test
    fun `when there is a company with a given id in db updateById with that id updates the company from db`() {

        insertCompanyInDb()

        securityContext.setAccessCode(112233)

        val result = companyService.updateById(company2.id, Company.parse(company3))

        val expected = company3.copy().apply { id = company2.id }

        assertEquals(Company.parse(expected ), result)
    }

    fun insertCompanyInDbForOpening() {
        codeRepository.save(codeEntityForCompany)
        companyRepository.save(company)
        companyCodeRepository.save(companyCodeEntity)
    }

    fun insertCompanyInDb() {
        codeRepository.save(codeEntityForCompany2)
        companyRepository.save(company2)
        companyCodeRepository.save(companyCodeEntity2)
    }

    fun insert2CompaniesInDb() {
        codeRepository.save(codeEntityForCompany2)
        companyRepository.save(company2)
        companyCodeRepository.save(companyCodeEntity2)

        codeRepository.save(codeEntityForCompany3)
        companyRepository.save(company3)
        companyCodeRepository.save(companyCodeEntity3)
    }
}
