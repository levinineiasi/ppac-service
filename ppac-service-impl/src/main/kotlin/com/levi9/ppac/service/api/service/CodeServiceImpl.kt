package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.AccessCode
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.random.Random

@Service
class CodeServiceImpl(
    val codeRepository: CodeRepository,
    val companyRepository: CompanyRepository,
    val companyCodeRepository: CompanyCodeRepository
) : CodeService<CompanyCode> {
    @Transactional
    override fun findAll(): List<CompanyCode> {
        return companyCodeRepository.findAll().map { CompanyCode.parse(it) }
    }

    override fun create(dto: CompanyCode): CompanyCode {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun createCompanyCode(displayName: String): CompanyCode {

        var value = Random.nextInt(100000, 999999);
        while (codeRepository.findByValue(value) != null) {
            value = Random.nextInt(100000, 999999);
        }

        val accessCodeDTO = AccessCode(UUID.randomUUID(), value)
        val savedAccessCode = codeRepository.save(AccessCode.parse(accessCodeDTO))

        val companyDTO = Company(UUID.randomUUID(), displayName)
        val savedCompany = companyRepository.save(Company.parse(companyDTO))

        val companyCodeDTO = CompanyCode(UUID.randomUUID(), AccessCode.parse(savedAccessCode), Company.parse(savedCompany));
        val savedCompanyCode = companyCodeRepository.save(CompanyCode.parse(companyCodeDTO))

        return CompanyCode.parse(savedCompanyCode)
    }


    @Transactional
    override fun deleteById(id: UUID) {
        companyCodeRepository.findByIdOrNull(id)?.let {
            companyCodeRepository.deleteById(id)
        }
    }
}