package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.data_classes.Trainer
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.repository.TrainerRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class OpeningServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val codeRepository: CodeRepository,
    private val openingRepository: OpeningRepository,
    private val companyRepository: CompanyRepository,
    private val trainerRepository: TrainerRepository
) : OpeningService<Opening> {

    override fun updateOpening(openingId: UUID, opening: Opening): Opening {

        openingRepository.findByIdOrNull(opening.id) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        val company =
            companyRepository.findFirstByOpenings_Id(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        if (!codeRepository.isCompanyCode(securityContext.getAccessCode(), company.id)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        opening.trainers.forEach {
            trainerRepository.findByIdOrNull(it.id) ?: trainerRepository.save(Trainer.parse(it.apply {
                id = UUID.randomUUID()
            }))
        }

        return Opening.parse(
            openingRepository.save(
                Opening.parse(
                    opening.apply { id = UUID.randomUUID() })
            )
        )
    }

    override fun availability(openingId: UUID, available: Boolean): Opening {

        val company =
            companyRepository.findFirstByOpenings_Id(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        if (!codeRepository.isCompanyCode(securityContext.getAccessCode(), company.id)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        openingRepository.updateAvailability(available, openingId)

        return Opening.parse(
            openingRepository.findByIdOrNull(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        )

    }

    override fun findAll(): List<Opening> {
        return openingRepository.findAll()
            .filter { it.available }
            .map { Opening.parse(it) }
    }

    override fun deleteById(id: UUID) {
        TODO("Not yet implemented")
    }
}