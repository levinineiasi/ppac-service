package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyCodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
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
    private val openingRepository: OpeningRepository,
    private val companyCodeRepository: CompanyCodeRepository
) : CompanyService<Company> {

    @Transactional
    override fun addOpening(id: UUID, opening: Opening): Opening {

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), id)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val companySet = opening.trainers.map { companyRepository.findCompanyEntitiesByOpeningsTrainersId(it.id) }
            .flatten()
            .toSet()

        if (companySet.isNotEmpty() && (companySet.size > 1 || companySet.first().id != id))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        val openingDTO = opening.apply { this.id = UUID.randomUUID() }
        val openingEntity = Opening.parse(openingDTO)
        val savedOpening = openingRepository.save(openingEntity)

        val companyEntity = companyRepository.findByIdOrNull(id)!!
        companyEntity.openings += savedOpening
        companyRepository.save(companyEntity)

        return Opening.parse(savedOpening)
    }

    @Transactional
    override fun findAll(): List<Company> {
        return companyRepository.findAll().map { companyEntity ->
            val activeOpenings = companyEntity.openings.filter { it.available }
            val companyEntityWithActiveOpenings = companyEntity.copy().apply { openings = activeOpenings }
            Company.parse(companyEntityWithActiveOpenings)
        }
    }

    @Transactional
    override fun findById(id: UUID): Company {
        val companyEntity = companyRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        val activeOpenings = companyEntity.openings.filter { it.available }
        val companyEntityWithActiveOpenings = companyEntity.copy().apply { openings = activeOpenings }
        return Company.parse(companyEntityWithActiveOpenings)
    }

    @Transactional
    override fun updateById(id: UUID, updatedObject: Company): Company {

        require(companyCodeRepository.isCompanyCode(id, securityContext.getAccessCode())) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val company = companyRepository.findById(id)
            .apply {
                this.get().name = updatedObject.name
                this.get().logo = updatedObject.logo
                this.get().description = updatedObject.description
                this.get().email = updatedObject.email
            }

        return Company.parse(companyRepository.save(company.get()))
    }

    @Transactional
    override fun deleteById(id: UUID) {

        require(codeRepository.isAdminCode(securityContext.getAccessCode())) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        companyRepository.findByIdOrNull(id)?.let {
            companyRepository.deleteById(id)
        }
    }
}
