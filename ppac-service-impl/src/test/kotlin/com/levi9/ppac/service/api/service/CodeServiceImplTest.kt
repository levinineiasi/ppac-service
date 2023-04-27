package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.service.config.TestConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
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
import org.webjars.NotFoundException
import java.util.UUID

@SpringBootTest(
    classes = [
        CodeServiceImpl::class
    ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@Import(TestConfig::class)
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
class CodeServiceImplTest {

    @Autowired
    lateinit var codeService: CodeService<Company, UUID>

    @Autowired
    lateinit var codeRepository: CodeRepository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    companion object {
        val codeEntityForAdmin = AccessCodeEntity(UUID.randomUUID(), 234567).apply { type = CodeType.ADMIN_CODE }
        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456)
        val company = CompanyEntity(UUID.randomUUID(), "Levi9", codeEntityForCompany)
    }

    @Test
    fun `isAdminCode SHOULD RETURN true WHEN admin code is correct`() {

        codeRepository.save(codeEntityForAdmin)

        Assertions.assertTrue(codeRepository.isAdminCode(codeEntityForAdmin.value))
    }

    @Test
    fun `isAdminCode SHOULD RETURN false WHEN admin code is incorrect`() {

        codeRepository.save(codeEntityForAdmin)

        Assertions.assertFalse(codeRepository.isAdminCode(234568))
    }

    @Test
    fun `isCompanyCode SHOULD RETURN true WHEN company code is correct and companyId match`() {

        companyRepository.save(company)

        Assertions.assertTrue(codeRepository.isCompanyCode(codeEntityForCompany.value, company.id))
    }

    @Test
    fun `isCompanyCode SHOULD RETURN false WHEN company code is incorrect and companyId doesn't`() {

        companyRepository.save(company)

        Assertions.assertFalse(codeRepository.isCompanyCode(654321, company.id))
    }


    @Test
    fun `isCompanyCode SHOULD RETURN false WHEN company code is correct and companyId doesn't match`() {

        companyRepository.save(company)

        Assertions.assertFalse(codeRepository.isCompanyCode(codeEntityForCompany.value, UUID.randomUUID()))
    }

    @Test
    fun `findAll SHOULD RETURN all company codes WHEN there are company codes in DB`() {

        codeRepository.save(codeEntityForAdmin)

        val company1 = codeService.createCompanyCode("Levi9")
        val company2 = codeService.createCompanyCode("Endava")
        val company3 = codeService.createCompanyCode("Eon")

        val result = codeService.findAll()

        assertEquals(3, result.size)

        assertEquals(company1, result[0])
        assertEquals(company2, result[1])
        assertEquals(company3, result[2])
    }

    @Test
    fun `findAll SHOULD RETURN no company codes WHEN there are no company codes in DB`() {

        codeRepository.save(codeEntityForAdmin)

        val result = codeService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `createCompanyCode SHOULD BE successful`() {

        codeRepository.save(codeEntityForAdmin)

        val companyCode = codeService.createCompanyCode("Levi9")

        val result = codeService.findAll()

        assertEquals(result.size, 1)

        assertEquals(companyCode.id, result[0].id)

        codeService.deleteById(companyCode.id)
    }

    @Test
    fun `deleteById SHOULD BE successful`() {

        codeRepository.save(codeEntityForAdmin)

        val companyCode = codeService.createCompanyCode("Levi9")

        codeService.deleteById(companyCode.id)

        val result = codeService.findAll()

        assertEquals(0, result.size)

    }

    @Test
    fun `deleteById SHOULD NOT delete anything from db WHEN company code doesn't exist in db`() {

        codeRepository.save(codeEntityForAdmin)

        codeService.createCompanyCode("Levi9")
        val id = UUID.randomUUID()

        try {
            codeService.deleteById(id)
        } catch (_: NotFoundException) {

        }

        val result = codeService.findAll()

        assertEquals(1, result.size)

    }
}
