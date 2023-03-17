package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.AccessCode
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.stereotype.Service
import org.webjars.NotFoundException
import java.util.*
import javax.naming.AuthenticationException
import javax.transaction.Transactional
import kotlin.random.Random

@Service
class CodeServiceImpl(
    val securityContext: SecurityContext<Int>,
    val codeRepository: CodeRepository,
    val companyRepository: CompanyRepository,
    val companyCodeRepository: CompanyCodeRepository,
) : CodeService<CompanyCode> {

    override fun findAll(): List<CompanyCode> {
        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }
        return companyCodeRepository.findAll().map { CompanyCode.parse(it) }
    }

    @Transactional
    override fun createCompanyCode(displayName: String): CompanyCode {

        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        var value = Random.nextInt(100000, 999999)
        while (codeRepository.isCodeIdPresent(value)) {
            value = Random.nextInt(100000, 999999)
        }

        logger.info("Generated $value value for $displayName company.")

        val accessCodeDTO = AccessCode(UUID.randomUUID(), value)
        val accessCodeEntity = codeRepository.save(AccessCode.parse(accessCodeDTO))

        logger.info("Inserted accessCodeEntity with id ${accessCodeEntity.id} into database.")

        val companyDTO = Company(UUID.randomUUID(), displayName)
        val companyEntity = companyRepository.save(Company.parse(companyDTO))

        logger.info("Inserted companyEntity with id ${companyEntity.id} into database.")

        val companyCodeDTO =
            CompanyCode(UUID.randomUUID(), AccessCode.parse(accessCodeEntity), Company.parse(companyEntity))
        val companyCodeEntity = companyCodeRepository.save(CompanyCode.parse(companyCodeDTO))

        logger.info("Inserted companyCodeEntity with id ${companyCodeEntity.id} into database.")

        return CompanyCode.parse(companyCodeEntity)
    }

    override fun checkAdminCode(){
        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }
    }

     override fun checkCompanyCode(companyId: UUID) {
        if (!codeRepository.isCompanyCode(securityContext.getAccessCode(), companyId)) {
            throw AuthenticationException()
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        companyCodeRepository.findById(id).orElseThrow { NotFoundException("The resource was not found") }
        companyCodeRepository.deleteById(id)
    }
}
