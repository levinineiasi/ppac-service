package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.repository.CodeRepository
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.OpeningRepository
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class OpeningServiceImpl(
    private val securityContext: SecurityContext<Int>,
    private val codeRepository: CodeRepository,
    private val openingRepository: OpeningRepository,
    private val companyRepository: CompanyRepository
) : OpeningService<Opening> {

    //TODO U can update trainer's details even if u are from other company when updating your opening
    @Transactional
    override fun updateOpening(openingId: UUID, opening: Opening): Opening {

        openingRepository.findByIdOrNull(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        val company =
            companyRepository.findFirstByOpenings_Id(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        if (!codeRepository.isCompanyCode(securityContext.getAccessCode(), company.id)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        return Opening.parse(
            openingRepository.save(
                Opening.parse(
                    opening.apply { id = openingId })
            )
        )
    }

    @Transactional
    override fun changeAvailability(openingId: UUID, available: Boolean): Opening {

        openingRepository.findByIdOrNull(openingId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

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

    @Transactional
    override fun findAll(): List<Opening> {
        return openingRepository.findAll()
            .filter { it.available }
            .map { Opening.parse(it) }
    }

}