package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.security.SecurityContext
import java.util.UUID
import javax.naming.AuthenticationException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
class CompanyServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val companyRepository: CompanyRepository,
    private val codeRepository: CodeRepository,
    private val openingRepository: OpeningRepository,
) : CompanyService<Company, UUID, Opening> {

    @Transactional
    override fun addOpening(id: UUID, opening: Opening): Opening {

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), id)) {
            throw AuthenticationException()
        }

        val companyEntity = companyRepository.findById(id).orElseThrow { NotFoundException() }

        val openingDTO = opening.apply { this.id = UUID.randomUUID() }
        val openingEntity = Opening.toEntity(openingDTO, companyEntity)
        if (openingEntity.views != 0) {
            openingEntity.views = 0
        }
        val savedOpening = openingRepository.save(openingEntity)

        companyEntity.openings += savedOpening
        companyRepository.save(companyEntity)

        return Opening.toBusinessModel(savedOpening)
    }

    @Transactional
    override fun findAll(): List<Company> {
        return companyRepository.findAll().map { companyEntity ->
            val activeOpenings = companyEntity.openings.filter { it.available }
            val companyEntityWithActiveOpenings = companyEntity.copy().apply {
                openings = activeOpenings
                name = companyEntity.name
                logo = companyEntity.logo
                description = companyEntity.description
                email = companyEntity.email
            }
            val companyList = Company.toBusinessModel(companyEntityWithActiveOpenings)
            companyList.accessCode = null
            companyList
        }
    }

    @Transactional
    override fun findById(id: UUID, onlyAvailableOpenings: Boolean): Company {
        val companyEntity = companyRepository.findById(id).orElseThrow { throw NotFoundException() }
        if (onlyAvailableOpenings) {
            val activeOpenings = companyEntity.openings.filter { it.available }
            val companyEntityWithActiveOpenings = companyEntity.copy().apply {
                openings = activeOpenings
                name = companyEntity.name
                logo = companyEntity.logo
                description = companyEntity.description
                email = companyEntity.email
            }
            val company = Company.toBusinessModel(companyEntityWithActiveOpenings)
            company.accessCode = null
            return company
        } else {

            val company = Company.toBusinessModel(companyEntity.apply {
                openings = companyEntity.openings
                name = companyEntity.name
                logo = companyEntity.logo
                description = companyEntity.description
                email = companyEntity.email
            })
            return company
        }
    }

    @Transactional
    override fun updateById(id: UUID, updatedObject: Company): Company {

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), id)) {
            throw AuthenticationException()
        }

        val updatedCompanyEntity = companyRepository.findById(id).orElseThrow { NotFoundException() }
        updatedCompanyEntity.apply {
            name = updatedObject.name
            logo = updatedObject.logo
            description = updatedObject.description
            email = updatedObject.email
        }

        return Company.toBusinessModel(companyRepository.save(updatedCompanyEntity))
    }

    @Transactional
    override fun deleteById(id: UUID) {

        require(codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw AuthenticationException()
        }

        companyRepository.findByIdOrNull(id)?.let {
            companyRepository.deleteById(id)
        }
    }
}
