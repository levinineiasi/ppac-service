package com.levi9.ppac.service.api.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size
import kotlin.collections.ArrayList

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
    @Size(min = 2, max = 50, message = "The email length should have between 2 and 300 characters.")
    @Email(message = "The company email should be a valid one.")
    var email: String? = null

    //@JsonManagedReference
    @OneToMany(cascade = [CascadeType.ALL])
    var openings: List<OpeningEntity> = ArrayList<OpeningEntity>()
}
