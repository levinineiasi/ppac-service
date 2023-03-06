package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CompanyCodeRepository : JpaRepository<CompanyCodeEntity,UUID>