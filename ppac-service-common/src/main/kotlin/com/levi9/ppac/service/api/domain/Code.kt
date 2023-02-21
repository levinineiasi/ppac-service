package com.levi9.ppac.service.api.domain

import com.levi9.ppac.service.api.enums.CodeType
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "CODES")
data class Code(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    var id: UUID
) {
    @Column(name = "VALUE")
    lateinit var value: String

    @Column(name = "CODE_TYPE")
    var type: CodeType? = CodeType.COMPANY_CODE

    @Column(name = "COMPANY_ID")
    var companyId: UUID? = null
}