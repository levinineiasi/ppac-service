package com.levi9.ppac.service.api.service

interface CrudService<T, ID> {
    fun findAll(): List<T>
    fun deleteById(id: ID) {
        throw NotImplementedError("Default implementation which throws NotImplementedError")
    }
}
