package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.business.Company

interface OpeningService<T, ID> : CrudService<T, ID> {
    fun findById(openingId: ID): T
    fun updateOpening(openingId: ID, opening: T): T
    fun changeAvailability(openingId: ID, available: Boolean): T
    fun findAllFirstCount(count: Int): List<T>
    fun getTotalCount(): Int
    fun getCompanyById(id: ID): Company
}
