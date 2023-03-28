package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.Opening
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
        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456).apply { type = CodeType.COMPANY_CODE }
        val trainer = TrainerEntity(UUID.randomUUID(), "Trainer1", "Some description for trainer 1")
        val openingToCreateTrainer = OpeningEntity(
            UUID.randomUUID(),
            emptyList(),
            emptyList(),
            false,
            false,
            4,
            PeriodType.MONTHS,
            20,
            true,
            false,
            listOf(trainer),
            true
        )
        val company = CompanyEntity(UUID.randomUUID(), "Levi9").apply { openings = listOf(openingToCreateTrainer) }
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
        val opening = OpeningEntity(
            UUID.randomUUID(),
            emptyList(),
            emptyList(),
            true,
            true,
            4,
            PeriodType.MONTHS,
            20,
            true,
            false,
            listOf(trainer),
            true
        )
    }

    @Test
    fun `when add opening, wrong access code for company given by id`() {

        insertCompanyInDb()

        assertThrows<ResponseStatusException> {
            companyService.addOpening(
                UUID.randomUUID(),
                Opening.Companion.parse(opening)
            )
        }
    }

    @Test
    fun `when add opening is successfully`() {

        insertCompanyInDb()

        securityContext.setAccessCode(companyCodeEntity.accessCode.value)

        val addOpening = companyService.addOpening(
            company.id,
            Opening.parse(opening)
        )

        Assertions.assertTrue(openingRepository.existsById(addOpening.id))

        val updatedCompany = companyRepository.findById(company.id)
        Assertions.assertTrue(updatedCompany.get().openings.contains(Opening.parse(addOpening)))
    }

    fun insertCompanyInDb() {
        codeRepository.save(codeEntityForCompany)
        companyRepository.save(company)
        companyCodeRepository.save(companyCodeEntity)
    }

}