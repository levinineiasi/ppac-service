package com.levi9.ppac.service.api.domain

import java.util.UUID
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

    @field:Id
    @Column(name = "ID", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "NAME", unique = true,  nullable = false)
    @field:Size(min = 2, max = 30, message = "Invalid length for name field.")
    var name: String
) {

    @Column(name = "LOGO", nullable = true)
    var logo: ByteArray? = null

    @Column(name = "DESCRIPTION", nullable = true)
    @field:Size(min = 40, max = 1000, message = "Invalid description for name field.")
    var description: String? = null

    @Column(name = "EMAIL", nullable = true)
    @field:Size(min = 5, max = 50, message = "Invalid length for email field.")
    @field:Email(message = "The company email should be a valid one.")
    var email: String? = null

    @OneToMany(cascade = [CascadeType.ALL])
    var openings: List<OpeningEntity> = ArrayList()
}
