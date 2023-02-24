package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.repository.CompanyRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
class CompanyServiceImpl(
    val companyRepository: CompanyRepository
) : CompanyService<Company> {
    @Transactional
    override fun findAll(): List<Company> {
        return companyRepository.findAll().map { Company.parse(it) }
    }

    override fun create(dto: Company): Company {
        TODO("Not yet implemented")
    }


    @Transactional
    override fun deleteById(id: UUID) {

        companyRepository.findByIdOrNull(id)?.let {
            companyRepository.deleteById(id)
        }
    }
}
