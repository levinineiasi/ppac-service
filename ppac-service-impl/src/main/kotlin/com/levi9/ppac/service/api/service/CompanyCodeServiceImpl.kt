package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class CompanyCodeServiceImpl(
    val companyCodeRepository: CompanyCodeRepository,
    val companyRepository: CompanyRepository,
    val codeRepository: CodeRepository
) : CompanyCodeService<CompanyCode> {

    override fun create(dto: CompanyCode): CompanyCode {

        println(codeRepository.existsById(dto.codeId))
        println(companyRepository.existsById(dto.companyId))
        if ( !codeRepository.existsById(dto.codeId) || !companyRepository.existsById(dto.companyId)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        return CompanyCode.parse(
            companyCodeRepository.save(
                CompanyCodeEntity(
                    UUID.randomUUID(),
                    dto.codeId,
                    dto.companyId
                )
            )
        )
    }

    override fun findAll(): List<CompanyCode> {
        return companyCodeRepository.findAll().map { CompanyCode.parse(it) }
    }

    override fun deleteById(id: UUID) {
        return companyCodeRepository.deleteById(id)
    }
}