package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name ="COMPANIES_CODES")
data class CompanyCodeEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CODE_ID")
    var code: UUID

){

    @Column(name = "COMPANY_ID", nullable = false)
    lateinit var companyId : UUID


}