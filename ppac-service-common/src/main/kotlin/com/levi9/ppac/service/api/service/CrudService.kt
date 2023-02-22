package com.levi9.ppac.service.api.service

import java.util.UUID

interface CrudService<T> {
    fun findAll(): List<T>
    fun create(dto: T): T
    fun deleteById(id: UUID)
}