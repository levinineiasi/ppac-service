package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.security.SecurityContext
import java.util.UUID
import javax.naming.AuthenticationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OpeningServiceImpl(
        private val securityContext: SecurityContext<Int>,
        private val codeRepository: CodeRepository,
        private val openingRepository: OpeningRepository,
        private val companyRepository: CompanyRepository
) : OpeningService<Opening> {

    @Transactional
    override fun updateOpening(openingId: UUID, opening: Opening): Opening {

        openingRepository.findByIdOrNull(openingId) ?: throw NotFoundException()

        val companyEntity =
                companyRepository.findFirstByOpeningsId(openingId)
                        ?: throw NotFoundException()

        require(codeRepository.isCompanyCode(securityContext.getAccessCode(), companyEntity.id)) {
            throw AuthenticationException()
        }

        val companySet = opening.trainers.map { companyRepository.findCompanyEntitiesByOpeningsTrainersId(it.id) }
                .flatten()
                .toSet()

        if (companySet.isNotEmpty() && (companySet.size > 1 || companySet.first().id != companyEntity.id)) {
            throw NotFoundException()
        }

        val openingEntity = Opening.parse(
                opening.apply { id = openingId })
        val updatedOpeningEntity = openingRepository.save(openingEntity)
        return Opening.parse(updatedOpeningEntity)
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

        return Opening.parse(
                openingRepository.save(openingEntity)
        )
    }

    @Transactional
    override fun findAll(): List<Opening> {
        return openingRepository.findAll()
                .filter { it.available }
                .map { Opening.parse(it) }
    }
}
