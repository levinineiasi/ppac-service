package com.levi9.ppac.service.api.service

import java.util.UUID

interface CodeService<T> : CrudService<T> {

    fun createCompanyCode(displayName: String): T

    fun isAdminCode(accessCode: Int): Boolean

    fun isCompanyCode(accessCode: Int, codeId: UUID): Boolean
}
