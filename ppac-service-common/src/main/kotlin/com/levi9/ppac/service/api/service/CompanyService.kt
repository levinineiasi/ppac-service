package com.levi9.ppac.service.api.service

import java.util.*

interface CompanyService<T> : CrudService<T> {
    fun findById(id: UUID): T
    fun updateById(id: UUID, updatedObject: T): T
}