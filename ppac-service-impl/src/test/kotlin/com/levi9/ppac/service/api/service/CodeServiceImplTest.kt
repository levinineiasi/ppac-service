package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.BootstrapWith
import java.util.UUID

@SpringBootTest(
    classes = [
        CodeServiceImpl::class
    ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
class CodeServiceImplTest {

    @Autowired
    lateinit var codeService: CodeService<CompanyCode>

    @Autowired
    lateinit var codeRepository: CodeRepository

    companion object {
        val adminCode = AccessCodeEntity(UUID.randomUUID(), 234567).apply { type = CodeType.ADMIN_CODE }
    }

    @BeforeEach
    fun `add admin code to database`() {

        codeRepository.save(adminCode)
    }

    @Test
    fun `when admin code is correct isAdmin returns true`() {

        Assertions.assertTrue(codeRepository.isAdminCode(adminCode.value))
    }

    @Test
    fun `when admin code is incorrect isAdmin returns false`() {

        Assertions.assertFalse(codeRepository.isAdminCode(234568))
    }

    @Test
    fun `when there are company codes in db findAll returns all company codes`() {

        val companyCode1 = codeService.createCompanyCode(adminCode.value, "Levi9")
        val companyCode2 = codeService.createCompanyCode(adminCode.value, "Endava")
        val companyCode3 = codeService.createCompanyCode(adminCode.value, "Eon")

        val result = codeService.findAll(adminCode.value)

        assertEquals(3, result.size)

        assertEquals(companyCode1, result[0])
        assertEquals(companyCode2, result[1])
        assertEquals(companyCode3, result[2])

        codeService.deleteById(adminCode.value, companyCode1.id)
        codeService.deleteById(adminCode.value, companyCode2.id)
        codeService.deleteById(adminCode.value, companyCode3.id)
    }

    @Test
    fun `when there are not company codes in db findAll returns no company codes`() {

        val result = codeService.findAll(adminCode.value)

        assertEquals(0, result.size)
    }

    @Test
    fun `when create company code it is inserted in db`() {

        val companyCode = codeService.createCompanyCode(adminCode.value, "Levi9")

        val result = codeService.findAll(adminCode.value)

        assertEquals(result.size, 1)

        assertEquals(companyCode.id, result[0].id)

        codeService.deleteById(adminCode.value, companyCode.id)
    }

    @Test
    fun `when delete company code it is deleted from db`() {

        val companyCode = codeService.createCompanyCode(adminCode.value, "Levi9")

        codeService.deleteById(adminCode.value, companyCode.id)

        val result = codeService.findAll(adminCode.value)

        assertEquals(0, result.size)

    }

    @Test
    fun `when delete company code which doesn't exist in db it doesn't delete anything from db`() {

        codeService.createCompanyCode(adminCode.value, "Levi9")
        val id = UUID.randomUUID()

        codeService.deleteById(adminCode.value, id)

        val result = codeService.findAll(adminCode.value)

        assertEquals(1, result.size)

    }
}