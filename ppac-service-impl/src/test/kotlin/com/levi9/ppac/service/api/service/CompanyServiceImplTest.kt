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
import com.levi9.ppac.service.api.security.SecurityContext
import com.levi9.ppac.service.api.service.config.TestConfig
import com.levi9.ppac.service.api.service.config.TestConfigForCompany
import org.junit.jupiter.api.Assertions
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
import org.springframework.web.server.ResponseStatusException
import java.util.*
import org.springframework.http.HttpStatus
import kotlin.Comparator

@SpringBootTest(
    classes = [
        CompanyServiceImpl::class
    ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@Import(TestConfigForCompany::class)
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
@TestPropertySource(properties = ["feature.mvp=true"])
class CompanyServiceImplTest {

    @Autowired
    lateinit var companyService: CompanyService<Company>

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
        private val openingToCreateTrainer = OpeningEntity(
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
        private val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(openingToCreateTrainer) }
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
        val openingAvailable = OpeningEntity(
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

        val openingUnavailable = OpeningEntity(
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

        val codeEntityForCompany2 = AccessCodeEntity(UUID.randomUUID(), 112233).apply { type = CodeType.COMPANY_CODE }
        val company2 = CompanyEntity(UUID.randomUUID(), "Amazon")
        val companyCodeEntity2 = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany2, company2)

        val codeEntityForCompany3 = AccessCodeEntity(UUID.randomUUID(), 223344).apply { type = CodeType.COMPANY_CODE }
        val company3 = CompanyEntity(UUID.randomUUID(), "Endava")
        val companyCodeEntity3 = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany3, company3)
    }

    @Test
    fun `when add opening, wrong access code for company given by id`() {

        insertCompanyInDb()

        assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.Companion.parse(openingAvailable)
            )
        }
    }


    @Test
    fun `when there are companies in db findAll returns all companies`() {

        insert2CompaniesInDb()

        val result = companyService.findAll()

        Assertions.assertEquals(2, result.size)

        Assertions.assertEquals(Company.parse(company2) , result[0])
        Assertions.assertEquals(Company.parse(company3) , result[1])
    }

    @Test
    fun `when there are no companies in db findAll returns no companies`() {

        val result = companyService.findAll()

        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun `when there is a company with a given id and no openings in db findById with that id and onlyAvailableOpenings true returns that company`() {

        insertCompanyInDb()

        val result = companyService.findById(company2.id, true)

        Assertions.assertEquals(Company.parse(company2), result)
    }

    @Test
    fun `when there isn't a company with a given id in db findById with that id and onlyAvailableOpenings true returns NOT_FOUND`() {

        Assertions.assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java)
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

        Assertions.assertEquals(expected, result)
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

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `when there is not a company with a given id in db updateById with that id returns 404 NotFound`() {

        insertCompanyInDb()

        Assertions.assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java)
        { companyService.updateById(company3.id, Company.parse(company3)) }
    }

    @Test
    fun `when there is a company with a given id in db updateById with that id updates the company from db`() {

        insertCompanyInDb()

        val result = companyService.updateById(company2.id, Company.parse(company3))

        val expected = company3.copy().apply { id = company2.id }

        Assertions.assertEquals(Company.parse(expected ), result)
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
