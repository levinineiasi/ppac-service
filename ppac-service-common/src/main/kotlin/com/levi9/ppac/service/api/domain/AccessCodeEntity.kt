package com.levi9.ppac.service.api.domain

import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.validator.annotations.ValidCodeType
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


@Entity
@Table(name = "CODES")
data class AccessCodeEntity(

    @field:NotNull
    @field:Id
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "VALUE", unique = true, nullable = false)
    @field:NotNull
    @field:Min(value = 100000, message = "Value should have 6 characters length.")
    @field:Max(value = 999999, message = "Value should have 6 characters length.")
    var value: Int,

    @Column(name = "CODE_TYPE")
    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    @field:ValidCodeType
    var type: CodeType = CodeType.COMPANY_CODE
)

