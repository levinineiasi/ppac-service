package com.levi9.ppac.service.api.service

interface CodeService<T> : CrudService<T>{

    fun createCompanyCode(displayName: String): T
}