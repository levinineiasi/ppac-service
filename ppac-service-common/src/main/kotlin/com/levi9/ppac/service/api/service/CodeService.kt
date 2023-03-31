package com.levi9.ppac.service.api.service

interface CodeService<T, ID> : CrudService<T, ID> {

    fun createCompanyCode(displayName: String): T

    fun checkAdminCode()

    fun checkCompanyCode(companyId: ID)
}
