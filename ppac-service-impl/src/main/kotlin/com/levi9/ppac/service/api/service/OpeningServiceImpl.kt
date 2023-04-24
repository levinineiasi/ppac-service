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

        val openingEnt = openingRepository.findByIdOrNull(openingId) ?: throw NotFoundException()

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), openingEnt.company.id)) {
            throw AuthenticationException()
        }

        val companyEntity = companyRepository.findByIdOrNull(opening.companyId)
        val openingEntity = Opening.toEntity(
            opening.apply { id = openingId }, companyEntity!!
        )
        val updatedOpeningEntity = openingRepository.save(openingEntity)
        return Opening.toBusinessModel(updatedOpeningEntity)
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

    override fun getCompanyById(id: UUID): Company{
        val companyEntity = companyRepository.findByIdOrNull(id) ?: throw NotFoundException()
        return Company.toBusinessModel(companyEntity)
    }
}
