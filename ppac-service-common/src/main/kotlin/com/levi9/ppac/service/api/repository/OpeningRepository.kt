package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.OpeningEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface OpeningRepository : PagingAndSortingRepository<OpeningEntity, UUID> {

    @Modifying
    @Query("UPDATE OpeningEntity o SET o.available = :availability WHERE o.id = :openingId")
    fun updateAvailability(availability: Boolean, openingId: UUID)
}