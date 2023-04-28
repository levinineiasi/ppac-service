package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.data.repository.findByIdOrNull

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.naming.AuthenticationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

@Service
class OpeningServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val codeRepository: CodeRepository,
    private val openingRepository: OpeningRepository,
    private val companyRepository: CompanyRepository
) : OpeningService<Opening, UUID> {

    @Transactional
    override fun updateOpening(openingId: UUID, opening: Opening): Opening {

        var openingEntity = openingRepository.findByIdOrNull(openingId) ?: throw NotFoundException()

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), openingEntity.company.id)) {
            throw AuthenticationException()
        }

        val companySet = opening.trainers.map { companyRepository.findCompanyEntitiesByOpeningsTrainersId(it.id) }
            .flatten()
            .toSet()

        if (companySet.isNotEmpty() && (companySet.size > 1 || companySet.first() !=  openingEntity.company)) {
            throw AuthenticationException()
        }

        opening.apply {
            companyId = openingEntity.company.id
        }

         openingEntity = Opening.toEntity(
            opening.apply { id = openingEntity.id }, openingEntity.company
        )

        val savedUpdatedOpeningEntity = openingRepository.save(openingEntity)
        return Opening.toBusinessModel(savedUpdatedOpeningEntity)
    }

    @Transactional
    override fun changeAvailability(openingId: UUID, available: Boolean): Opening {

        val openingEntity =
            openingRepository.findByIdOrNull(openingId) ?: throw NotFoundException()

        val companyEntity =
            companyRepository.findFirstByOpeningsId(openingId)
                ?: throw NotFoundException()

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), companyEntity.id)) {
            throw AuthenticationException()
        }

        openingEntity.available = available

        return Opening.toBusinessModel(
            openingRepository.save(openingEntity)
        )
    }

    override fun findById(openingId: UUID): Opening {
        val openingEntity = openingRepository.findByIdOrNull(openingId) ?: throw NotFoundException()
        openingEntity.views++
        openingRepository.save(openingEntity)
        return Opening.toBusinessModel(openingEntity)
    }

    @Transactional
    override fun findAll(): List<Opening> {
        return openingRepository.findAll()
            .filter { it.available }
            .sortedByDescending { it.views }
            .map { Opening.toBusinessModel(it) }
    }

    @Transactional
    override fun findAllFirstCount(count: Int): List<Opening> {
        return openingRepository.findAll()
                .filter { it.available }
                .sortedByDescending { it.views }
                .take(count)
                .map { Opening.toBusinessModel(it) }
    }

    override fun getTotalCount(): Int {
        return openingRepository.findAll()
                .filter { it.available }
                .size
    }

    override fun getCompanyById(id: UUID): Company {
        val companyEntity = companyRepository.findByIdOrNull(id) ?: throw NotFoundException()
        return Company.toBusinessModel(companyEntity)
    }
}
