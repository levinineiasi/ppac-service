package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompanyRepository : JpaRepository<CompanyEntity, UUID> {
    fun findFirstByOpeningsId(openingId: UUID): CompanyEntity?
    fun findCompanyEntitiesByOpeningsTrainersId(trainerId:UUID): List<CompanyEntity>
}
