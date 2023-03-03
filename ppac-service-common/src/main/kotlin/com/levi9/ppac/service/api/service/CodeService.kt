package com.levi9.ppac.service.api.service

interface CodeService<T> : CrudService<T> {

    fun createCompanyCode(adminCode: Int, displayName: String): T

    fun checkAdminCode(adminCode: Int): Boolean
}