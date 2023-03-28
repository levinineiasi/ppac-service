package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
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
import java.util.*

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
    lateinit var codeService: CodeService<CompanyCode>

    @Autowired
    lateinit var codeRepository: CodeRepository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var companyCodeRepository: CompanyCodeRepository

    companion object {
        val codeEntityForAdmin = AccessCodeEntity(UUID.randomUUID(), 234567).apply { type = CodeType.ADMIN_CODE }
        val codeEntityForCompany = AccessCodeEntity(UUID.randomUUID(), 123456).apply { type = CodeType.COMPANY_CODE }
        val company = CompanyEntity(UUID.randomUUID(), "Levi9")
        val companyCodeEntity = CompanyCodeEntity(UUID.randomUUID(), codeEntityForCompany, company)
    }

    @Test
    fun `when admin code is correct isAdminCode returns true`() {

        codeRepository.save(codeEntityForAdmin)

        Assertions.assertTrue(codeRepository.isAdminCode(codeEntityForAdmin.value))
    }

    @Test
    fun `when admin code is incorrect isAdminCode returns false`() {

        codeRepository.save(codeEntityForAdmin)

        Assertions.assertFalse(codeRepository.isAdminCode(234568))
    }

    @Test
    fun `when company code is correct and companyId match isCompanyCode returns true`() {

        insertCompanyInDb()

        Assertions.assertTrue(codeRepository.isCompanyCode(codeEntityForCompany.value, company.id))
    }

    @Test
    fun `when company code is incorrect and companyId doesn't isCompanyCode returns false`() {

        insertCompanyInDb()

        Assertions.assertFalse(codeRepository.isCompanyCode(654321, company.id))
    }


    @Test
    fun `when company code is correct and companyId doesn't match isCompanyCode returns false`() {

        insertCompanyInDb()

        Assertions.assertFalse(codeRepository.isCompanyCode(codeEntityForCompany.value, UUID.randomUUID()))
    }

    @Test
    fun `when there are company codes in db findAll returns all company codes`() {

        codeRepository.save(codeEntityForAdmin)

        val companyCode1 = codeService.createCompanyCode("Levi9")
        val companyCode2 = codeService.createCompanyCode("Endava")
        val companyCode3 = codeService.createCompanyCode("Eon")

        val result = codeService.findAll()

        assertEquals(3, result.size)

        assertEquals(companyCode1, result[0])
        assertEquals(companyCode2, result[1])
        assertEquals(companyCode3, result[2])

        codeService.deleteById(companyCode1.id)
        codeService.deleteById(companyCode2.id)
        codeService.deleteById(companyCode3.id)
    }

    @Test
    fun `when there are not company codes in db findAll returns no company codes`() {

        codeRepository.save(codeEntityForAdmin)

        val result = codeService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `when create company code it is inserted in db`() {

        codeRepository.save(codeEntityForAdmin)

        val companyCode = codeService.createCompanyCode("Levi9")

        val result = codeService.findAll()

        assertEquals(result.size, 1)

        assertEquals(companyCode.id, result[0].id)

        codeService.deleteById(companyCode.id)
    }

    @Test
    fun `when delete company code it is deleted from db`() {

        codeRepository.save(codeEntityForAdmin)

        val companyCode = codeService.createCompanyCode("Levi9")

        codeService.deleteById(companyCode.id)

        val result = codeService.findAll()

        assertEquals(0, result.size)

    }

    @Test
    fun `when delete company code which doesn't exist in db it doesn't delete anything from db`() {

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

    fun insertCompanyInDb() {
        codeRepository.save(codeEntityForCompany)
        companyRepository.save(company)
        companyCodeRepository.save(companyCodeEntity)
    }
}
