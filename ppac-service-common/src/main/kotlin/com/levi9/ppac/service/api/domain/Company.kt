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
    var displayName: String? = null

    @Column(name = "full_name")
    var fullName: String? = null

    @Column(name = "logo")
    var logo: ByteArray? = null

    @OneToMany(mappedBy = "companyId", fetch = FetchType.LAZY)
    var students: Set<Student>? = null
}
