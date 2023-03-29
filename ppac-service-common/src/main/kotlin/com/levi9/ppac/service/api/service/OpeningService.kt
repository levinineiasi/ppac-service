package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Opening
import java.util.UUID

interface OpeningService<T> {
    fun updateOpening(openingId: UUID, opening: Opening): T
    fun findAll(): List<T>
    fun changeAvailability(openingId: UUID, available: Boolean): T
}
