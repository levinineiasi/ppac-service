package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
interface CompanyCodeRepository : JpaRepository<CompanyCodeEntity,UUID>
