package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "students")
data class Student (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: UUID
) {
    @Column(name = "first_name")
    lateinit var firstName: String

    @Column(name = "last_name")
    lateinit var lastName: String

    @Column(name = "company_id")
    var companyId: UUID? = null
}
