package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.naming.AuthenticationException

@Service
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
class CompanyServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val companyRepository: CompanyRepository,
    private val codeRepository: CodeRepository
) : CompanyService<Company> {
    @Transactional
    override fun findAll(): List<Company> {

        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        return companyRepository.findAll().map { Company.parse(it) }
    }

    @Transactional
    override fun deleteById(id: UUID) {

        if (!codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        companyRepository.findByIdOrNull(id)?.let {
            companyRepository.deleteById(id)
        }
    }
}
