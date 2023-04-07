package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.business.Trainer
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
import java.util.UUID

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
    lateinit var openingService: OpeningService<Opening, UUID>

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

        val trainerEntity = TrainerEntity(UUID.randomUUID(), "Bob Smith", "Bob has 5 years experience in Java")
        val openingEntity = OpeningEntity(
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
            trainers = emptyList(),
            available = true
        )
        var openingEntityWithTrainer = openingEntity.copy().apply {
            id = UUID.randomUUID()
            trainers = listOf(trainerEntity)
        }
        val accessCodeEntity = AccessCodeEntity(UUID.randomUUID(), 123456)
        val companyEntity = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(openingEntity) }
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), accessCodeEntity, companyEntity)
        val accessCodeEntity2 = AccessCodeEntity(UUID.randomUUID(), 123457)
        val companyEntity2 = CompanyEntity(UUID.randomUUID(), "Amazon").apply {
            openings = listOf(
                openingEntityWithTrainer
            )
        }
        val companyCodeEntity2 =
            CompanyCodeEntity(UUID.randomUUID(), accessCodeEntity2, companyEntity2)
    }

    @Test
    fun `findAll SHOULD RETURN all openings WHEN there are openings in DB`() {

        openingRepository.save(openingEntityWithTrainer)
        openingRepository.save(openingEntity)

        val result = openingService.findAll()

        assertEquals(2, result.size)
        assertEquals(Opening.parse(openingEntity), result[1])
    }

    @Test
    fun `updateOpening SHOULD BE successful`() {

        insertCompanyInDb(accessCodeEntity, companyEntity , companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val updatedOpening = openingService.updateOpening(
            openingEntity.id,
            Opening.parse(openingEntityWithTrainer)
        )

        val expectedOpeningEntity = openingEntityWithTrainer.copy().apply { this.id = openingEntity.id }
        val expectedTrainer = Trainer.parse(trainerEntity)

        assertEquals(Opening.parse(expectedOpeningEntity), updatedOpening)
        assertTrue(updatedOpening.trainers.contains(expectedTrainer))
    }

    @Test
    fun `updateOpening SHOULD RETURN UNAUTHORIZED WHEN AccessCode is invalid`() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                openingEntity.id,
                Opening.parse(openingEntityWithTrainer)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
    }

    @Test
    fun `updateOpening SHOULD RETURN BAD_REQUEST WHEN opening has an invalid openingId`() {

        insertCompanyInDb(accessCodeEntity2, companyEntity2, companyCodeEntity2)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                UUID.randomUUID(),
                Opening.parse(openingEntity)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `updateOpening SHOULD RETURN BAD_REQUEST WHEN opening is not from company`() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)

        openingRepository.save(openingEntityWithTrainer)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                companyEntity.id,
                Opening.parse(openingEntityWithTrainer)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `updateOpening SHOULD RETURN BAD_REQUEST WHEN opening has a trainer from another company `() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)
        insertCompanyInDb(accessCodeEntity2, companyEntity2, companyCodeEntity2)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val exception = assertThrows<ResponseStatusException> {
            openingService.updateOpening(
                openingEntity.id,
                Opening.parse(openingEntityWithTrainer)
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `changeAvailability SHOULD BE successful`() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val response = openingService.changeAvailability(
            openingEntity.id,
            false
        )

        val openingEntityUnavailable = openingEntity.copy().apply { available = false }
        val expectedResponse = Opening.parse(openingEntityUnavailable)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `changeAvailability SHOULD RETURN BAD_REQUEST WHEN opening is not in DB`() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                openingEntityWithTrainer.id,
                false
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `changeAvailability SHOULD RETURN BAD_REQUEST WHEN opening's company is not in DB`() {

        openingRepository.save(openingEntity)

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                openingEntity.id,
                false
            )
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Test
    fun `changeAvailability SHOULD RETURN UNAUTHORIZED WHEN AccessCode is invalid`() {

        insertCompanyInDb(accessCodeEntity, companyEntity, companyCodeEntity)

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            openingService.changeAvailability(
                openingEntity.id,
                false
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)
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
