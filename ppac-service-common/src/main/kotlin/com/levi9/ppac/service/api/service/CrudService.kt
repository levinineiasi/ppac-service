package com.levi9.ppac.service.api.service

interface CrudService<T, ID> {
    fun findAll(): List<T>
    fun findAllFirstCount(count: Int): List<T>{
        throw NotImplementedError("Default implementation which throws NotImplementedError")
    }
    fun deleteById(id: ID) {
        throw NotImplementedError("Default implementation which throws NotImplementedError")
    }
}
