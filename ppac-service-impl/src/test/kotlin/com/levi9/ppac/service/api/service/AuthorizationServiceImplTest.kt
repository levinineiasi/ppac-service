package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
import java.util.*

@SpringBootTest(
        classes = [
            AuthorizationServiceImpl::class
        ]
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
@EntityScan(basePackages = ["com.levi9.ppac.service.api.domain"])
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
class AuthorizationServiceImplTest {
    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var codeRepository: CodeRepository

    @BeforeEach
    fun `add admin code to database`() {
        val adminCode = AccessCodeEntity(UUID.randomUUID(), 234567)
        adminCode.type = CodeType.ADMIN_CODE
        codeRepository.save(adminCode)
    }

    @Test
    fun `when admin code is correct isAdmin returns true`() {

        assertTrue(authorizationService.isAdmin(234567))
    }

    @Test
    fun `when admin code is incorrect isAdmin returns false`() {

        assertFalse(authorizationService.isAdmin(123456))
    }
}