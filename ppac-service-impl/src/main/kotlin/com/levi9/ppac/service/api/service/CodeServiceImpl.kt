package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Code
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CodeServiceImpl(
    val codeRepository: CodeRepository,
    val companyRepository: CompanyRepository
) : CodeService<Code> {

    @Transactional
    override fun findAll(): List<Code> {
        return codeRepository.findAll().map { Code.parse(it) }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        codeRepository.findByIdOrNull(id)?.let {
            codeRepository.deleteById(id)
        }
    }

    @Transactional
    override fun create(dto: Code, displayName: String): Code {

        val company = Company().apply { this.displayName = displayName }

        val persistedCode = codeRepository.save(
            Code.parse(dto).apply {
                id = UUID.randomUUID()
                companyId = companyRepository.save(Company.parse(company)).id
            }
        )

        return Code.parse(persistedCode)
    }


    override fun create(dto: Code): Code {
        TODO("Not yet implemented")
    }
}