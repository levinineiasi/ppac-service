package com.levi9.ppac.service.api.service

interface CodeService<T, ID> : CrudService<T, ID> {

    fun createCompanyCode(displayName: String): T

    fun isAdminCode(accessCode: Int): Boolean

    fun isCompanyCode(accessCode: Int, companyId: ID): Boolean
}
