package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
class CompanyServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val companyRepository: CompanyRepository,
    private val codeRepository: CodeRepository,
    private val companyCodeRepository: CompanyCodeRepository
) : CompanyService<Company> {

    @Transactional
    override fun findAll(): List<Company> {
        return companyRepository.findAll().map { Company.parse(it) }
    }

    @Transactional
    override fun findById(id: UUID): Company {
        val company = companyRepository.findById(id).orElseThrow {ResponseStatusException(HttpStatus.NOT_FOUND)}
        return Company.parse(company)
    }

    @Transactional
    override fun updateById(id: UUID, updatedObject: Company): Company {
        if (!companyCodeRepository.isCompanyCode(id, securityContext.getAccessCode())) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val company = companyRepository.findById(id)
                .apply{
                    this.get().name = updatedObject.name
                    this.get().logo = updatedObject.logo
                    this.get().description = updatedObject.description
                    this.get().email = updatedObject.email
                }

        return Company.parse(companyRepository.save(company.get()))
    }

    @Transactional
    override fun deleteById(id: UUID) {

        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        companyRepository.findByIdOrNull(id)?.let {
            companyRepository.deleteById(id)
        }
    }
}
