package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.repository.CodeRepository
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
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
import org.springframework.transaction.annotation.Transactional
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
    lateinit var codeRepository: CodeRepository

    @Autowired
    lateinit var codeService: CodeService<CompanyCode>

    @Test
    fun `my example test`() {
        codeService.createCompanyCode("Levi9")
        codeService.createCompanyCode("Endava")
        codeService.createCompanyCode("Eon")
        var smth = codeService.findAll()
        var x = 9
    }
}