package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "COMPANIES_CODES")
data class CompanyCodeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    var id: UUID,
    @OneToOne
    @JoinColumn(name = "CODE_ID", referencedColumnName = "ID")
    val codeId: AccessCodeEntity,

    @OneToOne
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    val companyId: CompanyEntity

)