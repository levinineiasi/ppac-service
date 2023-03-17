package com.levi9.ppac.service.api.service

import java.util.UUID

interface CodeService<T> : CrudService<T> {

    fun createCompanyCode(displayName: String): T

    fun checkAdminCode()

    fun checkCompanyCode(companyId: UUID)
}
