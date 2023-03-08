package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "COMPANIES_CODES")
data class CompanyCodeEntity(

    @Id
    @Column(name = "ID")
    var id: UUID,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "CODE_ID", referencedColumnName = "ID")
    val accessCode: AccessCodeEntity,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    val company: CompanyEntity

)
