package com.levi9.ppac.service.api.domain

import com.levi9.ppac.service.api.enums.CodeType
import java.util.*
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

    @Id
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "VALUE", unique = true, nullable = false)
    @NotNull
    @Min(value = 100000, message = "Value should have minimum 6 characters.")
    @Max(value = 999999, message = "Value should have maximum 6 characters.")
    var value: Int
) {

    @Column(name = "CODE_TYPE")
    @Enumerated(EnumType.STRING)
//    @Pattern(regexp = "(ADMIN_CODE|COMPANY_CODE)", message = "CodeType should have value ADMIN_CODE or COMPANY_CODE.")
    var type: CodeType? = CodeType.COMPANY_CODE
}