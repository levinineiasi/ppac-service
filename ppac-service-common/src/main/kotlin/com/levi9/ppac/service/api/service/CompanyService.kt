package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Opening
import java.util.UUID

interface CompanyService<T> : CrudService<T> {
    fun addOpening(id: UUID, opening: Opening): Opening
    fun findById(id: UUID, onlyAvailableOpenings: Boolean): T
    fun updateById(id: UUID, updatedObject: T): T
}
