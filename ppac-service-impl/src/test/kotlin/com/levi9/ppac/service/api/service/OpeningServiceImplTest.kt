package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.business.Trainer
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.domain.TrainerEntity
import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.repository.CodeRepository
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
import org.springframework.test.context.BootstrapWith
import org.springframework.test.context.TestPropertySource
import java.util.UUID
import javax.naming.AuthenticationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

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
    lateinit var securityContext: SecurityContext<Int>

    companion object {

        val trainerEntity =
            TrainerEntity(UUID.randomUUID(), "Bob Smith", "Bob Smith has 5 years experience as Java Developer")
        val accessCodeEntity = AccessCodeEntity(UUID.randomUUID(), 123456)
        val companyEntity = CompanyEntity(UUID.randomUUID(), "Levi9", accessCodeEntity)
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
            available = true,
            company = companyEntity
        )
        val accessCodeEntity2 = AccessCodeEntity(UUID.randomUUID(), 123457)
        val companyEntity2 = CompanyEntity(UUID.randomUUID(), "Amazon", accessCodeEntity2)
        var openingEntityWithTrainer = openingEntity.copy().apply {
            id = UUID.randomUUID()
            trainers = listOf(trainerEntity)
            company = companyEntity
        }
    }

    @Test
    fun `findAll SHOULD RETURN all openings WHEN there are openings in DB`() {

        openingRepository.save(openingEntityWithTrainer)
        openingRepository.save(openingEntity)

        val result = openingService.findAll()

        assertEquals(2, result.size)
        assertEquals(Opening.toBusinessModel(openingEntity), result[1])
    }

//    @Test
//    fun `updateOpening SHOULD BE successful`() {
//
//        openingRepository.save(openingEntity)
//
//        securityContext.setAccessCode(companyEntity.accessCode.value)
//
//        val updatedOpeningEntity = openingEntity.copy().apply {
//            this.trainers = listOf(trainerEntity)
//        }
//
//        val updatedOpening = openingService.updateOpening(
//            openingEntity.id, Opening.toBusinessModel(updatedOpeningEntity))
//
//        assertEquals(Opening.toBusinessModel(updatedOpeningEntity), updatedOpening)
//    }

    @Test
    fun `updateOpening SHOULD THROW AuthenticationException WHEN AccessCode is invalid`() {

        insertCompanyInDb(accessCodeEntity, companyEntity)

        openingRepository.save(openingEntity)

        securityContext.setAccessCode(accessCodeEntity.value + 1)

        assertThrows<AuthenticationException> {
            openingService.updateOpening(
                openingEntity.id,
                Opening.toBusinessModel(openingEntityWithTrainer)
            )
        }
    }

    @Test
    fun `updateOpening SHOULD RETURN BAD_REQUEST WHEN opening has an invalid openingId`() {

        insertCompanyInDb(accessCodeEntity2, companyEntity2)

        assertThrows<NotFoundException> {
            openingService.updateOpening(
                UUID.randomUUID(),
                Opening.toBusinessModel(openingEntity)
            )
        }
    }

    @Test
    fun `updateOpening SHOULD THROW NotFoundException WHEN opening is not from company`() {

        insertCompanyInDb(accessCodeEntity, companyEntity)

        openingRepository.save(openingEntityWithTrainer)

        assertThrows<NotFoundException> {
            openingService.updateOpening(
                companyEntity.id,
                Opening.toBusinessModel(openingEntityWithTrainer)
            )
        }
    }

//    @Test
//    fun `updateOpening SHOULD throw AuthenticationException WHEN opening has a trainer from another company `() {
//
//        insertCompanyInDb(accessCodeEntity, companyEntity)
//        insertCompanyInDb(accessCodeEntity2, companyEntity2)
//
//        openingRepository.save(openingEntity)
//
//        securityContext.setAccessCode(accessCodeEntity.value)
//
//        assertThrows<AuthenticationException> {
//            openingService.updateOpening(
//                openingEntity.id,
//                Opening.toBusinessModel(openingEntityWithTrainer)
//            )
//        }
//    }

    @Test
    fun `changeAvailability SHOULD BE successful`() {

        companyEntity.openings += openingEntity

        insertCompanyInDb(accessCodeEntity, companyEntity)

        securityContext.setAccessCode(accessCodeEntity.value)

        val response = openingService.changeAvailability(
            openingEntity.id,
            false
        )

        val openingEntityUnavailable = openingEntity.copy().apply { available = false }
        val expectedResponse = Opening.toBusinessModel(openingEntityUnavailable)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `changeAvailability SHOULD THROW NotFoundException WHEN opening is not in DB`() {

        assertThrows<NotFoundException> {
            openingService.changeAvailability(
                openingEntityWithTrainer.id,
                false
            )
        }
    }

    @Test
    fun `changeAvailability SHOULD THROW NotFoundException WHEN opening's company is not in DB`() {

        openingRepository.save(openingEntity)

        assertThrows<NotFoundException> {
            openingService.changeAvailability(
                UUID.randomUUID(),
                false
            )
        }
    }

    @Test
    fun `changeAvailability SHOULD THROW AuthenticationException WHEN AccessCode is invalid`() {

        securityContext.setAccessCode(111111)

        openingEntity.company = companyEntity2

        companyEntity2.openings = listOf(openingEntity)

        insertCompanyInDb(accessCodeEntity2, companyEntity2)

        assertThrows<AuthenticationException> {
            openingService.changeAvailability(
                openingEntity.id,
                false
            )
        }
    }

    fun insertCompanyInDb(
        accessCodeEntity: AccessCodeEntity,
        companyEntity: CompanyEntity,
    ) {
        codeRepository.save(accessCodeEntity)
        companyRepository.save(companyEntity)
    }
}
