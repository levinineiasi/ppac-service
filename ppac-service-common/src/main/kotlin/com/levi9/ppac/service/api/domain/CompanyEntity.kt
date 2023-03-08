package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "COMPANIES")
data class CompanyEntity(
    @Id
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "DISPLAY_NAME", nullable = false)
    @Size(min = 2, max = 30, message = "The displayed name length should have between 2 and 30 characters.")
    var displayName: String
) {

    @Column(name = "FULL_NAME", nullable = true)
    @Size(min = 2, max = 50, message = "The full name length should have between 2 and 50 characters.")
    var fullName: String? = null

    @Column(name = "LOGO", nullable = true)
    var logo: ByteArray? = null
}
