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
            val companyEntityWithActiveOpenings = companyEntity.copy()
                    .apply {
                        openings = activeOpenings
                        name = companyEntity.name
                        logo = companyEntity.logo
                        description = companyEntity.description
                        email = companyEntity.email
                    }
            Company.parse(companyEntityWithActiveOpenings)
        }
    }

    @Transactional
    override fun findById(id: UUID, onlyAvailableOpenings: Boolean): Company {
        val companyEntity = companyRepository.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (onlyAvailableOpenings) {
            val activeOpenings = companyEntity.openings.filter { it.available }
            val companyEntityWithActiveOpenings = companyEntity.copy()
                    .apply {
                        openings = activeOpenings
                        name = companyEntity.name
                        logo = companyEntity.logo
                        description = companyEntity.description
                        email = companyEntity.email
                    }
            return Company.parse(companyEntityWithActiveOpenings)
        } else {
            return Company.parse(companyEntity
                    .apply {
                        openings = companyEntity.openings
                        name = companyEntity.name
                        logo = companyEntity.logo
                        description = companyEntity.description
                        email = companyEntity.email
                    })
        }
    }

    @Transactional
    override fun updateById(id: UUID, updatedObject: Company): Company {

        require(companyCodeRepository.isCompanyCode(id, securityContext.getAccessCode())) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val updatedCompanyEntity = companyRepository.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        updatedCompanyEntity.apply {
            name = updatedObject.name
            logo = updatedObject.logo
            description = updatedObject.description
            email = updatedObject.email
        }

        return Company.parse(companyRepository.save(updatedCompanyEntity))
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
