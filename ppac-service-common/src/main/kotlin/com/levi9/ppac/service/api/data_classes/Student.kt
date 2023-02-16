package com.levi9.ppac.service.api.data_classes

import java.util.*
import com.levi9.ppac.service.api.domain.Student as StudentEntity
class Student(
    val id: UUID = UUID.randomUUID()
) {
    lateinit var firstName: String

    lateinit var lastName: String

    var companyId: UUID? = null

    companion object {
        fun parse(elem: StudentEntity): Student {
            return Student(elem.id).apply {
                firstName = elem.firstName
                lastName = elem.lastName
                companyId = elem.companyId
            }
        }

        fun parse(elem: Student): StudentEntity {
            return StudentEntity(elem.id).apply {
                firstName = elem.firstName
                lastName = elem.lastName
                companyId = elem.companyId
            }
        }
    }
}