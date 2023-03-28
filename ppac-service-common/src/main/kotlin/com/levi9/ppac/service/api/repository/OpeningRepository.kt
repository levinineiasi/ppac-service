package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.OpeningEntity
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.UUID

interface OpeningRepository : PagingAndSortingRepository<OpeningEntity, UUID>
