package com.levi9.ppac.service.api.service

import java.util.UUID

interface CrudService<T> {
    fun findAll(): List<T>
    fun findById(id: UUID): T
    fun updateById(id: UUID, updatedObject: T): T
    fun deleteById(id: UUID)
}
