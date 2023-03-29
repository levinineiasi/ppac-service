//package com.levi9.ppac.service.api.service
//
//import com.levi9.ppac.service.api.business.Opening
//import com.levi9.ppac.service.api.domain.AccessCodeEntity
//import com.levi9.ppac.service.api.domain.CompanyCodeEntity
//import com.levi9.ppac.service.api.domain.CompanyEntity
//import com.levi9.ppac.service.api.domain.OpeningEntity
//import com.levi9.ppac.service.api.domain.TrainerEntity
//import com.levi9.ppac.service.api.enums.CodeType
//import com.levi9.ppac.service.api.enums.PeriodType
//import com.levi9.ppac.service.api.repository.CodeRepository
//import com.levi9.ppac.service.api.repository.CompanyCodeRepository
//import com.levi9.ppac.service.api.repository.CompanyRepository
//import com.levi9.ppac.service.api.repository.OpeningRepository
//import com.levi9.ppac.service.api.security.SecurityContext
//import com.levi9.ppac.service.api.service.config.TestConfig
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration
//import org.springframework.boot.autoconfigure.domain.EntityScan
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
//import org.springframework.context.annotation.Import
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.test.context.BootstrapWith
//import org.springframework.test.context.TestPropertySource
//import java.util.*
//
//@SpringBootTest(
//    classes = [
//        OpeningServiceImpl::class
//    ]
//)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableAutoConfiguration
//@Import(TestConfig::class)
//@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
//@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
//@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
//@TestPropertySource(properties = ["feature.mvp=true"])
//class OpeningServiceImplTest {
//
//    @Autowired
//    lateinit var openingService: OpeningService<Opening>
//
//    @Autowired
//    lateinit var companyRepository: CompanyRepository
//
//    @Autowired
//    lateinit var codeRepository: CodeRepository
//
//    @Autowired
//    lateinit var openingRepository: OpeningRepository
//
//    @Autowired
//    lateinit var companyCodeRepository: CompanyCodeRepository
//
//    @Autowired
//    lateinit var securityContext: SecurityContext<Int>
//
//    companion object {
//        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456).apply { type = CodeType.COMPANY_CODE }
//        val trainer = TrainerEntity(UUID.randomUUID(), "Trainer1", "Some description for trainer 1")
//        val openingToCreateTrainer = OpeningEntity(
//            UUID.randomUUID(),
//            emptyList(),
//            emptyList(),
//            false,
//            false,
//            4,
//            PeriodType.MONTHS,
//            20,
//            true,
//            false,
//            listOf(trainer),
//            true
//        )
//        val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(openingToCreateTrainer) }
//        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
//        val opening = Opening(
//            UUID.randomUUID(),
//            emptyList(),
//            emptyList(),
//            true,
//            true,
//            4,
//            PeriodType.MONTHS,
//            20,
//            true,
//            false,
//            listOf(),
//            true
//        )
//    }
//
//    @Test
//    fun `when there are openings in db findAll returns all openings`() {
//
//        val openingUnavailable = opening.copy().apply {
//            id = UUID.randomUUID()
//            available = false
//            acceptOnClosingOpportunity = false
//        }
//        val openingAvailable = openingUnavailable.copy().apply {
//            id = UUID.randomUUID()
//            available = true
//        }
//
//        val openingEntity = Opening.parse(opening)
//        val openingUnavailableEntity = Opening.parse(openingUnavailable)
//        val openingAvailableEntity = Opening.parse(openingAvailable)
//        openingRepository.save(openingEntity)
//        openingRepository.save(openingUnavailableEntity)
//        openingRepository.save(openingAvailableEntity)
//
//        val result = openingService.findAll()
//
//        Assertions.assertEquals(2, result.size)
//
//        Assertions.assertEquals(openingAvailable,result[1])
//
//    }
//
//    fun insertCompanyInDb() {
//        codeRepository.save(codeEntityForCompany)
//        companyRepository.save(company)
//        companyCodeRepository.save(companyCodeEntity)
//    }
//
//
//}