package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Entity
@Table(name = "COMPANIES")
data class CompanyEntity(
    @Id
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "NAME", nullable = false)
    @Size(min = 2, max = 30, message = "The name length should have between 2 and 30 characters.")
    var name: String
) {

    @Column(name = "LOGO", nullable = true)
    var logo: ByteArray? = null

    @Column(name = "DESCRIPTION", nullable = true)
    @Size(min = 2, max = 300, message = "The description length should have between 2 and 300 characters.")
    var description: String? = null

    @Column(name = "EMAIL", nullable = true)
    @Size(min = 5, max = 50, message = "The email length should have between 5 and 50 characters.")
    @Email(message = "The company email should be a valid one.")
    var email: String? = null

    @OneToMany(cascade = [CascadeType.ALL])
    var openings: List<OpeningEntity> = ArrayList<OpeningEntity>()
}
