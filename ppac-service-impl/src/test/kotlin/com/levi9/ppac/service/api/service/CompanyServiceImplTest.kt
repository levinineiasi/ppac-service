package com.levi9.ppac.service.api.service

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
import java.util.*

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
    lateinit var companyService: CompanyService<Company,UUID,Opening>

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
        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456).apply { type = CodeType.COMPANY_CODE }
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
        val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(opening) }
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
    }

    @Test
    fun `when add opening, no access code for company given by id`() {

        insertCompanyInDb()

        val exception = assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)

//        deleteFromDb()
    }

    @Test
    fun `when add opening, wrong access code for company given by id`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value + 1)

        val exception = assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.parse(opening)
            )
        }

        assertEquals(HttpStatus.UNAUTHORIZED, exception.status)

//        deleteFromDb()
    }

    @Test
    fun `when add opening is successful`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val addOpening = companyService.addOpening(
            company.id,
            Opening.parse(opening)
        )

        assertNotNull(openingRepository.findById(addOpening.id))

        val updatedCompany = companyRepository.findById(company.id)
        assertTrue(updatedCompany.get().openings.map { it.id }.contains(addOpening.id))
        assertNotNull(trainerRepository.findById(trainer.id))

//TODO : When delete => error
//        deleteFromDb()
    }

    @Test
    fun `when add opening but trainer is from another company`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val companySameTrainer = company.copy().apply {
            id = UUID.randomUUID()
            openings = listOf(opening.copy().apply { id = UUID.randomUUID() })
        }
        companyRepository.save(companySameTrainer)

//        val exception = assertThrows<ResponseStatusException> {
//            companyService.addOpening(
//                company.id,
//                Opening.parse(opening)
//            )
//        }

        val addOpening = companyService.addOpening(
                company.id,
                Opening.parse(opening)
            )


//            assertEquals(HttpStatus.BAD_REQUEST, exception.status)

//        deleteFromDb()

    }

    fun insertCompanyInDb() {
        codeRepository.save(codeEntityForCompany)
        companyRepository.save(company)
        companyCodeRepository.save(companyCodeEntity)
    }

//    fun deleteFromDb() {
//        openingRepository.deleteAll()
//        trainerRepository.deleteAll()
//        companyCodeRepository.deleteAll()
//        companyRepository.deleteAll()
//        codeRepository.deleteAll()
//    }

}