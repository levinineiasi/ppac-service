package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.CompanyCode
import org.junit.jupiter.api.Assertions.assertEquals
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

    @Test
    fun `when there are company codes in db findAll returns all company codes`() {
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
        val result = codeService.findAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `when create company code it is inserted in db`() {
        val companyCode = codeService.createCompanyCode("Levi9")

        val result = codeService.findAll()

        assertEquals(result.size, 1)

        assertEquals(companyCode, result[0])

        codeService.deleteById(companyCode.id)
    }

//    @Test
//    fun `when create company code with display name length shorter than 2 is not inserted in db`() {
//        val companyCode = codeService.createCompanyCode("L")
//
//        val result = codeService.findAll()
//
//        assertEquals(0, result.size)
//    } // asta va merge dupa ce va functiona size-ul de la display name

    @Test
    fun `when delete company code it is deleted from db`() {
        val companyCode = codeService.createCompanyCode("Levi9")

        codeService.deleteById(companyCode.id)

        val result = codeService.findAll()

        assertEquals(0, result.size)

    }

    @Test
    fun `when delete company code which doesn't exist in db it doesn't delete anything from db`() {
        codeService.createCompanyCode("Levi9")
        val id = UUID.randomUUID()

        codeService.deleteById(id)

        val result = codeService.findAll()

        assertEquals(1, result.size)

    }
}