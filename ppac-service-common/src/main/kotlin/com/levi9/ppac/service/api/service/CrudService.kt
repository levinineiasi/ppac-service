package com.levi9.ppac.service.api.service

import java.util.UUID

interface CrudService<T> {
    fun findAll(adminCode: Int): List<T>
    fun deleteById(adminCode: Int,id: UUID)
}