import com.levi9.ppac.service.api.service.CompanyService
import com.levi9.ppac.service.api.service.CompanyServiceImpl
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.domain.TrainerEntity
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
import org.springframework.test.context.BootstrapWith
import org.springframework.test.context.TestPropertySource
import java.util.UUID
import javax.naming.AuthenticationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

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
        private val trainer = TrainerEntity(UUID.randomUUID(), "Bob Smith", "Bob Smith is a Java developer for 5 years")
        private val openingAvailable = OpeningEntity(
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
        private val openingUnavailable = openingAvailable.copy().apply {
            id = UUID.randomUUID()
            available = false
        }
        private val accessCodeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456)
        private val companyEntity =
            CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(openingAvailable) }
        private val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), accessCodeEntityForCompany, companyEntity)

        private val accessCodeEntityForCompany2 = AccessCodeEntity(UUID.randomUUID(), 112233)
        private val companyEntity2 = CompanyEntity(UUID.randomUUID(), "Amazon")
        private val companyCodeEntity2 =
            CompanyCodeEntity(UUID.randomUUID(), accessCodeEntityForCompany2, companyEntity2)

        private val accessCodeEntityForCompany3 = AccessCodeEntity(UUID.randomUUID(), 223344)
        private val companyEntity3 = CompanyEntity(UUID.randomUUID(), "Facebook")
        private val companyCodeEntity3 =
            CompanyCodeEntity(UUID.randomUUID(), accessCodeEntityForCompany3, companyEntity3)
    }

    @Test
    fun `addOpening SHOULD BE successful`() {

        insertCompanyInDb(accessCodeEntityForCompany, companyEntity, companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val newOpening = companyService.addOpening(
            companyEntity.id,
            Opening.toBusinessModel(openingAvailable)
        )

        assertNotNull(openingRepository.findById(newOpening.id))

        val updatedCompany = companyRepository.findById(companyEntity.id)
        val openingIds = updatedCompany.get().openings.map { it.id }

        assertTrue(openingIds.contains(newOpening.id))
        assertNotNull(trainerRepository.findById(trainer.id))
    }

    @Test
    fun `addOpening SHOULD THROW AuthenticationException WHEN AccessCode is not set`() {

        insertCompanyInDb(accessCodeEntityForCompany, companyEntity, companyCodeEntity)

        assertThrows<AuthenticationException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.toBusinessModel(openingAvailable)
            )
        }
    }

    @Test
    fun `addOpening SHOULD THROW AuthenticationException WHEN AccessCode is invalid`() {

        insertCompanyInDb(accessCodeEntityForCompany, companyEntity, companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        assertThrows<AuthenticationException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.toBusinessModel(openingAvailable)
            )
        }
    }

    @Test
    fun `addOpening SHOULD THROW NotFoundException WHEN trainer belong to another company`() {

        val openingWithSameTrainer = openingAvailable.copy().apply { id = UUID.randomUUID() }

        insertCompanyInDb(accessCodeEntityForCompany, companyEntity, companyCodeEntity)
        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)

        securityContext.setAccessCode(companyCodeEntity2.accessCode.value)

        assertThrows<NotFoundException> {
            companyService.addOpening(
                companyEntity2.id,
                Opening.toBusinessModel(openingWithSameTrainer)
            )
        }
    }

    @Test
    fun `findAll SHOULD RETURN all companies WHEN multiple companies exit in DB`() {

        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)
        insertCompanyInDb(accessCodeEntityForCompany3, companyEntity3, companyCodeEntity3)

        val result = companyService.findAll()

        assertEquals(2, result.size)

        assertEquals(Company.toBusinessModel(companyEntity2), result[0])
        assertEquals(Company.toBusinessModel(companyEntity3), result[1])
    }

    @Test
    fun `findAll SHOULD RETURN emptyList WHEN there are no companies in DB`() {

        val result = companyService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `findById SHOULD RETURN company with only available openings WHEN onlyAvailable = true`() {

        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)

        securityContext.setAccessCode(accessCodeEntityForCompany2.value)

        companyService.addOpening(companyEntity2.id, Opening.toBusinessModel(openingAvailable))
        companyService.addOpening(companyEntity2.id, Opening.toBusinessModel(openingUnavailable))

        val result = companyService.findById(companyEntity2.id, true)
        val expected = Company.toBusinessModel(companyEntity2.copy().apply { openings = listOf(openingAvailable) })

        assertEquals(expected, result)
    }

    @Test
    fun `findById SHOULD RETURN company with all openings WHEN onlyAvailable = false`() {

        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)

        securityContext.setAccessCode(accessCodeEntityForCompany2.value)

        companyService.addOpening(companyEntity2.id, Opening.toBusinessModel(openingAvailable))
        companyService.addOpening(companyEntity2.id, Opening.toBusinessModel(openingUnavailable))

        val result = companyService.findById(companyEntity2.id, false)

        val expected =
            Company.toBusinessModel(
                companyEntity2.copy().apply { openings = listOf(openingAvailable, openingUnavailable) })

        assertEquals(expected, result)
    }

    @Test
    fun `findById SHOULD THROW NotFoundException WHEN companyID is not present in DB`() {

        insertCompanyInDb(accessCodeEntityForCompany, companyEntity, companyCodeEntity)

        assertThrows(NotFoundException::class.java)
        { companyService.findById(companyEntity2.id, true) }
    }

    @Test
    fun `updateById SHOULD BE successful`() {

        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)

        securityContext.setAccessCode(accessCodeEntityForCompany2.value)

        val result = companyService.updateById(companyEntity2.id, Company.toBusinessModel(companyEntity3))

        val expected = companyEntity3.copy().apply { id = companyEntity2.id }

        assertEquals(Company.toBusinessModel(expected), result)
    }

    @Test
    fun `updatedById SHOULD THROW AuthenticationException WHEN companyID is not present in DB`() {

        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2, companyCodeEntity2)

        assertThrows(AuthenticationException::class.java)
        { companyService.updateById(companyEntity3.id, Company.toBusinessModel(companyEntity3)) }
    }

    fun insertCompanyInDb(
        accessCodeEntity: AccessCodeEntity,
        companyEntity: CompanyEntity,
        companyCodeEntity: CompanyCodeEntity
    ) {
        codeRepository.save(accessCodeEntity)
        companyRepository.save(companyEntity)
        companyCodeRepository.save(companyCodeEntity)
    }
}
