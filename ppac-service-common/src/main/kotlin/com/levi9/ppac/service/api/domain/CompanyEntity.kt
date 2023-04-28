package com.levi9.ppac.service.api.domain

import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
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
    @field:Size(min = 2, max = 150, message = "Invalid length for name field.")
    var name: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "CODE_ID", referencedColumnName = "ID")
    var accessCode: AccessCodeEntity
) {

    @Column(name = "LOGO", nullable = true)
    var logo: ByteArray? = null

    @Column(name = "DESCRIPTION", nullable = true)
    @field:Size(min = 20, max = 3000, message = "Invalid length for description field.")
    var description: String? = null

    @Column(name = "EMAIL", nullable = true)
    @field:Size(min = 5, max = 50, message = "Invalid length for email field.")
    @field:Email(message = "The company email should be a valid one.")
    var email: String? = null

    @OneToMany(fetch =  FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "company")
    var openings: List<OpeningEntity> = ArrayList()
}
