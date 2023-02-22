package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "COMPANIES")
data class CompanyEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    var id: UUID
) {
    @Column(name = "DISPLAY_NAME")
    var displayName: String? = null

    @Column(name = "FULL_NAME")
    var fullName: String? = null

    @Column(name = "LOGO")
    var logo: ByteArray? = null

}
