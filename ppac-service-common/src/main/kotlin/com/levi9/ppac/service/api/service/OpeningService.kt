package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Opening
import java.util.UUID

interface OpeningService<T> : CrudService<T> {
    fun updateOpening(openingId: UUID, opening: Opening) : T

    fun availability(openingId: UUID, available : Boolean) : T
}