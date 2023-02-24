package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.AccessCode
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.random.Random

@Service
class CodeServiceImpl(
    val codeRepository: CodeRepository,
    val companyRepository: CompanyRepository
) : CodeService<AccessCode> {
    @Transactional
    override fun findAll(): List<AccessCode> {
        return codeRepository.findAll().map { AccessCode.parse(it) }
    }

    override fun create(dto: AccessCode): AccessCode {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun createCompanyCode(adminValue: Int, displayName: String): AccessCode {

        val accessCodeDTO = AccessCode(UUID.randomUUID(), Random.nextInt(100000, 999999))
        codeRepository.save(AccessCode.parse(accessCodeDTO))
        val companyDTO = Company(UUID.randomUUID(), displayName)
        companyRepository.save(Company.parse(companyDTO))
        return accessCodeDTO
    }


    @Transactional
    override fun deleteById(id: UUID) {
        codeRepository.findByIdOrNull(id)?.let {
            codeRepository.deleteById(id)
        }
    }
}