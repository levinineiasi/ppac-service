package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CompanyRepository: JpaRepository<CompanyEntity, UUID>
