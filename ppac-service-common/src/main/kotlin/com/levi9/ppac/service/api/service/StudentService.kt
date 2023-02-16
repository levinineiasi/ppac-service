package com.levi9.ppac.service.api.service

import java.util.UUID

interface StudentService<T> : CrudService<T> {
    fun assignToCompany(studentId: UUID, companyId: UUID): Boolean
}
