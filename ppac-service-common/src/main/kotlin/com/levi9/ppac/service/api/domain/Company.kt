package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "companies")
data class Company (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: UUID
) {
    @Column(name = "display_name")
    lateinit var displayName: String

    @Column(name = "full_name")
    lateinit var fullName: String

    @Column(name = "logo")
    var logo: ByteArray? = null

    @OneToMany(mappedBy = "companyId")
    lateinit var students: Set<Student>
}
