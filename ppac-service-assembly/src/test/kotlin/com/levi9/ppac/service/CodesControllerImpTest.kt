package com.levi9.ppac.service

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.BootstrapWith
import java.util.*


@SpringBootTest(
    classes = [ApiApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
class CodesControllerImpTest {


    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var codeRepository: CodeRepository

    @BeforeEach
    fun adminCode() {

        codeRepository.deleteAll()
        codeRepository.save(AccessCodeEntity(UUID.randomUUID(), 123456, CodeType.ADMIN_CODE))
    }

    @Test
    fun whePostCalled_thenShouldReturnBankObject() {


        println(codeRepository.findAll())

        val headers = HttpHeaders()
        headers.set("AccessCode", "123456")
        val entity = HttpEntity(null, headers)
        val result = restTemplate.exchange(
            "http://localhost:8080/api/v1/codes/checkAdminCode", HttpMethod.GET,
            entity,
            String::class.java
        )
        println(result)
        println(codeRepository.findAll())

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)

    }
}