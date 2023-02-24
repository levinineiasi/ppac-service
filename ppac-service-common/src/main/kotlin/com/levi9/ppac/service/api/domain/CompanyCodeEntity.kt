package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "COMPANIES_CODES")
data class CompanyCodeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "CODE_ID", nullable = false, unique = true)
    var codeId: UUID,

    @Column(name = "COMPANY_ID", nullable = false, unique = true)
    var companyId: UUID
)