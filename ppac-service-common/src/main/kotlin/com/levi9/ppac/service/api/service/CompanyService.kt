package com.levi9.ppac.service.api.service

interface CompanyService<T, ID, S> : CrudService<T, ID> {
    fun addOpening(id: ID, opening: S): S
    fun findById(id: ID, onlyAvailableOpenings: Boolean): T
    fun updateById(id: ID, updatedObject: T): T
}
