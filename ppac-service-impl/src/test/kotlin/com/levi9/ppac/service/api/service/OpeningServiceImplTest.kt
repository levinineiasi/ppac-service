package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.business.Trainer
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
import java.util.*

@SpringBootTest(
    classes = [
        OpeningServiceImpl::class
    ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@Import(TestConfig::class)
@EntityScan(
    basePackages = ["com.levi9.ppac.service.api.domain"]
)
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
@TestPropertySource(properties = ["feature.mvp=true"])
class OpeningServiceImplTest {

    @Autowired
    lateinit var openingService: OpeningService<Opening,UUID>

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
        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456)
        val codeEntityForCompanyWithTrainer =
            AccessCodeEntity(UUID.randomUUID(), 123457).apply { type = CodeType.COMPANY_CODE }
        val trainer = TrainerEntity(UUID.randomUUID(), "Trainer1", "Some description for trainer 1")
        val companyOpening = OpeningEntity(
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
            available = true
        )
        var opening = companyOpening.copy().apply {
            id = UUID.randomUUID()
            trainers = listOf(trainer)
            hasTechnicalInterview = true
            hasTechnicalTest = true
        }
        val companyOpeningTrainer = companyOpening.copy().apply {
            id = UUID.randomUUID()
            trainers = listOf(trainer)
        }
        val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(companyOpening) }
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
        val companyWithTrainer = CompanyEntity(UUID.randomUUID(), "Levi9").apply {
            openings = listOf(
                companyOpeningTrainer
            )
        }
        val companyWithTrainerCodeEntity =
            CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompanyWithTrainer, companyWithTrainer)
    }

    @Test
    fun `when there are openings in db findAll returns all openings`() {


        val openingUnavailable = opening.copy().apply {
            id = UUID.randomUUID()
            available = false
            acceptOnClosingOpportunity = false
            trainers = listOf()
        }
        val openingAvailable = openingUnavailable.copy().apply {
            id = UUID.randomUUID()
            available = true
        }

        openingRepository.save(opening.copy().apply { trainers = emptyList() })
        openingRepository.save(openingUnavailable)
        openingRepository.save(openingAvailable)

        val result = openingService.findAll()

        assertEquals(2, result.size)
        assertEquals(Opening.parse(openingAvailable), result[1])

    }


    @Test
    fun `when update opening, no code for company opening given by id`() {

        insertCompanyInDb()

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                companyOpening.id,
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    fun `when update opening, wrong code for company opening given by id`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                companyOpening.id,
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    fun `when update opening, company opening given by id is wrong`() {

        insertCompanyInDb()

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                UUID.randomUUID(),
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `when update opening, opening given by id does not have a company `() {

        insertCompanyInDb()

        openingRepository.save(opening)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                opening.id,
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `update opening successfully`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val updatedOpening = openingService.updateOpening(
            companyOpening.id,
            Opening.parse(opening)
        )

        val expectedOpeningEntity = opening.copy().apply { this.id = companyOpening.id }
        val expectedTrainer = Trainer.parse(trainer)

        assertEquals(Opening.parse(expectedOpeningEntity), updatedOpening)
        assertTrue(updatedOpening.trainers.contains(expectedTrainer))
    }

    @Test
    fun `when update opening, opening given have a trainer from another company `() {

        codeRepository.save(codeEntityForCompanyWithTrainer)
        companyRepository.save(companyWithTrainer)
        companyCodeRepository.save(companyWithTrainerCodeEntity)

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                companyOpening.id,
                Opening.parse(companyOpeningTrainer)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `when change availability, opening not found`() {

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                companyOpening.id,
                false
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `when change availability, company not found`() {

        openingRepository.save(companyOpening)

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                companyOpening.id,
                false
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)

        openingRepository.deleteAll()
    }

    @Test
    fun `change availability of opening successfully`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val response = openingService.changeAvailability(
            companyOpening.id,
            false
        )

        val expectedResponse = Opening.parse(companyOpening.copy().apply { available = false })
        assertEquals(expectedResponse, response)
    }


    @Test
    fun `when change availability, wrong access code for company`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                companyOpening.id,
                false
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    fun insertCompanyInDb() {
        codeRepository.save(codeEntityForCompany)
        companyRepository.save(company)
        companyCodeRepository.save(companyCodeEntity)
    }

}
