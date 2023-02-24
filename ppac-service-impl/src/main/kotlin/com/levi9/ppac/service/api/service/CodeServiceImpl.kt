package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.AccessCode
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class CodeServiceImpl(
    val codeRepository: CodeRepository
) : CodeService<AccessCode> {

    @Transactional
    override fun findAll(): List<AccessCode> {
        return codeRepository.findAll().map { AccessCode.parse(it) }
    }

    @Transactional
    override fun create(dto: AccessCode): AccessCode {

        val persistedAccessCode = codeRepository.save(
            AccessCode.parse(dto).apply {
                id = UUID.randomUUID()
            }
        )

        return AccessCode.parse(persistedAccessCode)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        codeRepository.findByIdOrNull(id)?.let {
            codeRepository.deleteById(id)
        }
    }
}