package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.Company as CompanyEntity
import java.util.UUID

class Company(
    val id: UUID = UUID.randomUUID()
) {
    lateinit var displayName: String

    lateinit var fullName: String

    var logo: ByteArray? = null

    var students: Set<Student> = emptySet()

    companion object {
        fun parse(elem: CompanyEntity): Company {
            return Company(elem.id).apply {
                displayName = elem.displayName
                fullName = elem.fullName
                logo = elem.logo
                students = elem.students.map { Student.parse(it) }.toSet()
            }
        }

        fun parse(elem: Company): CompanyEntity {
            return CompanyEntity(elem.id).apply {
                displayName = elem.displayName
                fullName = elem.fullName
                logo = elem.logo
                students = elem.students.map { Student.parse(it) }.toSet()
            }
        }
    }
}