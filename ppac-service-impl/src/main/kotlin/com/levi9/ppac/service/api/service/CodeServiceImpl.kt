package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.AccessCode
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.CompanyCode
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.stereotype.Service
import org.webjars.NotFoundException
import java.util.UUID
import javax.naming.AuthenticationException
import javax.transaction.Transactional
import kotlin.random.Random

const val MINIM_VALUE: Int = 100000
const val MAXIM_VALUE: Int = 999999
@Service
class CodeServiceImpl(
    val securityContext: SecurityContext<Int>,
    val codeRepository: CodeRepository,
    val companyRepository: CompanyRepository,
    val companyCodeRepository: CompanyCodeRepository
) : CodeService<CompanyCode, UUID> {

    override fun findAll(): List<CompanyCode> {
        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }
        return companyCodeRepository.findAll().map { CompanyCode.toBusinessModel(it) }
    }

    override fun createCompanyCode(name: String): CompanyCode {

        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        var valueNr = Random.nextInt(MINIM_VALUE, MAXIM_VALUE)
        while (codeRepository.isCodeIdPresent(valueNr)) {
            valueNr = Random.nextInt(MINIM_VALUE, MAXIM_VALUE)
        }

        logger.info("Generated $valueNr value for $name company.")

        val accessCodeDTO = AccessCode(UUID.randomUUID(),valueNr,CodeType.COMPANY_CODE)
        val accessCodeEntity = codeRepository.save(AccessCode.toEntity(accessCodeDTO))


        logger.info("Inserted accessCodeEntity with id ${accessCodeEntity.id} into database.")

        val companyDTO = Company(UUID.randomUUID(), name)
        val companyEntity = companyRepository.save(Company.toEntity(companyDTO))

        logger.info("Inserted companyEntity with id ${companyEntity.id} into database.")

        val companyCodeDTO =
            CompanyCode(
                UUID.randomUUID(),
                AccessCode.toBusinessModel(accessCodeEntity),
                Company.toBusinessModel(companyEntity)
            )
        val companyCodeEntity = companyCodeRepository.save(CompanyCode.toEntity(companyCodeDTO))

        logger.info("Inserted companyCodeEntity with id ${companyCodeEntity.id} into database.")

        return CompanyCode.toBusinessModel(companyCodeEntity)
    }

    override fun checkAdminCode() {
        require(codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }
    }

    override fun checkCompanyCode(companyId: UUID) {
        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), companyId)) {
            throw AuthenticationException()
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        require(codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        companyCodeRepository.findById(id).orElseThrow { NotFoundException("The resource was not found") }
        companyCodeRepository.deleteById(id)
    }
}
