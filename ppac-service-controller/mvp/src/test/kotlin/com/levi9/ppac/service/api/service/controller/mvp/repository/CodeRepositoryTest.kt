package com.levi9.ppac.service.api.service.controller.mvp.repository

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.BootstrapWith
import java.util.*
import kotlin.test.assertTrue

@DataJpaTest
@EntityScan(basePackages = ["com.levi9.ppac.service.api.repository"])
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = ["com.levi9.ppac.service.api.repository"])
@BootstrapWith(value = SpringBootTestContextBootstrapper::class)
class CodeRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var codeRepository: CodeRepository

    @Test
    fun WhenIsAdminCode_thenReturnTrue() {
        val accessCodeEntity = AccessCodeEntity(UUID.randomUUID(), 123456, CodeType.ADMIN_CODE)
        entityManager.persist(accessCodeEntity)
        entityManager.flush()
        val isAdminCode = codeRepository.isAdminCode(accessCodeEntity.value)

        assertTrue(isAdminCode)
    }

    @Test
    fun WhenIsCodeIdPresent_thenReturnTrue() {
        val accessCodeEntity = AccessCodeEntity(UUID.randomUUID(), 123456)
        entityManager.persist(accessCodeEntity)
        entityManager.flush()
        val isCodeIdPresent = codeRepository.isCodeIdPresent(accessCodeEntity.value)

        assertTrue(isCodeIdPresent)
    }

    @Test
    fun WhenIsCompanyCode_thenReturnTrue() {
        val accessCodeEntity = AccessCodeEntity(UUID.randomUUID(), 123456)
        entityManager.persist(accessCodeEntity)
        entityManager.flush()
        val isCompanyCode = codeRepository.isCompanyCode(accessCodeEntity.value,accessCodeEntity.id)

        assertTrue(isCompanyCode)
    }

}