import com.levi9.ppac.service.api.service.CompanyService
import com.levi9.ppac.service.api.service.CompanyServiceImpl
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.domain.TrainerEntity
import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.repository.TrainerRepository
import com.levi9.ppac.service.api.security.SecurityContext
import com.levi9.ppac.service.api.service.config.TestConfig
import org.junit.jupiter.api.Test
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
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
    lateinit var openingRepository: OpeningRepository

    @Autowired
    lateinit var securityContext: SecurityContext<Int>

    companion object {
        private val trainer = TrainerEntity(UUID.randomUUID(), "Bob Smith", "Bob Smith is a Java developer for 5 years")
        private val accessCodeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456)
        private val companyEntity =
            CompanyEntity(UUID.randomUUID(), "Levi9", accessCodeEntityForCompany)
        val openingAvailable = OpeningEntity(
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
            trainers = listOf(),
            available = true,
            company = companyEntity
        )
        private val openingUnavailable = openingAvailable.copy().apply {
            id = UUID.randomUUID()
            available = false
            company = companyEntity
        }
        val companyEntityWithOpenings = companyEntity.copy().apply { openings = listOf(openingAvailable) }
        private val accessCodeEntityForCompany2 = AccessCodeEntity(UUID.randomUUID(), 112233)

        private val companyEntity2 = CompanyEntity(UUID.randomUUID(), "Amazon", accessCodeEntityForCompany2)

        private val companyEntity3 = CompanyEntity(UUID.randomUUID(), "Facebook", accessCodeEntityForCompany2)
    }

    @Test
    fun `addOpening SHOULD BE successful`() {

        companyRepository.save(companyEntity)

        securityContext.setAccessCode(accessCodeEntityForCompany.value)

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

        companyRepository.save(companyEntity)

        assertThrows<AuthenticationException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.toBusinessModel(openingAvailable)
            )
        }
    }

    @Test
    fun `addOpening SHOULD THROW AuthenticationException WHEN AccessCode is invalid`() {

        companyRepository.save(companyEntity)

        securityContext.setAccessCode(accessCodeEntityForCompany.value + 1)

        assertThrows<AuthenticationException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.toBusinessModel(openingAvailable)
            )
        }
    }

//    @Test
//    fun `addOpening SHOULD THROW NotFoundException WHEN trainer belong to another company`() {
//
//        val openingWithSameTrainer = openingAvailable.copy().apply { id = UUID.randomUUID() }
//
//        insertCompanyInDb(accessCodeEntityForCompany, companyEntity)
//        insertCompanyInDb(accessCodeEntityForCompany2, companyEntity2)
//
//        securityContext.setAccessCode(accessCodeEntityForCompany2.value)
//
//        assertThrows<NotFoundException> {
//            companyService.addOpening(
//                companyEntity2.id,
//                Opening.toBusinessModel(openingWithSameTrainer)
//            )
//        }
//    }

    @Test
    fun `findAll SHOULD RETURN all companies WHEN multiple companies exit in DB`() {

        companyRepository.save(companyEntity2)
        companyRepository.save(companyEntity3)

        val result = companyService.findAll()

        assertEquals(2, result.size)

        val companyEntity2DTO = Company.toBusinessModel(companyEntity2)
        val companyEntity3DTO = Company.toBusinessModel(companyEntity3)

        companyEntity2DTO.accessCode = null
        companyEntity3DTO.accessCode = null

        assertEquals(companyEntity2DTO, result[0])
        assertEquals(companyEntity3DTO, result[1])
    }

    @Test
    fun `findAll SHOULD RETURN emptyList WHEN there are no companies in DB`() {

        val result = companyService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `findById SHOULD RETURN company with only available openings WHEN onlyAvailable = true`() {

        companyRepository.save(companyEntity)

        securityContext.setAccessCode(companyEntity.accessCode.value)

        companyService.addOpening(companyEntityWithOpenings.id, Opening.toBusinessModel(openingAvailable))
        companyService.addOpening(companyEntityWithOpenings.id, Opening.toBusinessModel(openingUnavailable))

        val result = companyService.findById(companyEntityWithOpenings.id, true)
        openingAvailable.id = result.openings!![0].id
        val expected = Company.toBusinessModel(companyEntityWithOpenings.copy().apply { openings = listOf(openingAvailable) })
        expected.accessCode = null

        assertEquals(expected, result)
    }

    @Test
    fun `findById SHOULD RETURN company with all openings WHEN onlyAvailable = false`() {

        companyEntity.openings = listOf(openingAvailable)


        companyRepository.save(companyEntity)

        securityContext.setAccessCode(accessCodeEntityForCompany.value)

        openingRepository.save(openingAvailable)

        openingRepository.save(openingUnavailable)

        val result = companyService.findById(companyEntity.id, false)

        val expected =
            Company.toBusinessModel(
                companyEntity.copy().apply { openings = listOf(openingAvailable, openingUnavailable) })

        assertEquals(expected, result)
    }

    @Test
    fun `findById SHOULD THROW NotFoundException WHEN companyID is not present in DB`() {

        companyRepository.save(companyEntity)

        assertThrows(NotFoundException::class.java)
        { companyService.findById(companyEntity2.id, true) }
    }

    @Test
    fun `updateById SHOULD BE successful`() {

        companyRepository.save(companyEntity2)

        securityContext.setAccessCode(accessCodeEntityForCompany2.value)

        val result = companyService.updateById(companyEntity2.id, Company.toBusinessModel(companyEntity3))

        val expected = companyEntity3.copy().apply { id = companyEntity2.id }

        val expectedDTO = Company.toBusinessModel(expected)

        assertEquals(expectedDTO, result)
    }

    @Test
    fun `updatedById SHOULD THROW AuthenticationException WHEN companyID is not present in DB`() {

        companyRepository.save(companyEntity2)

        assertThrows(AuthenticationException::class.java)
        { companyService.updateById(companyEntity3.id, Company.toBusinessModel(companyEntity3)) }
    }
}
