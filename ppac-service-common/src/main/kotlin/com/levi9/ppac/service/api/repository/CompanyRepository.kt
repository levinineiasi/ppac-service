package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompanyRepository : JpaRepository<CompanyEntity, UUID> {
    fun findFirstByOpenings_Id(openingId: UUID): CompanyEntity?
    fun findCompanyEntitiesByOpenings_Trainers_Id(trainerId:UUID): List<CompanyEntity>
}
