package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.*

class CompanyCode(
    val id: UUID = UUID.randomUUID()
) {
    lateinit var codeId: UUID

    lateinit var companyId: UUID

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            return CompanyCode(elem.id).apply {
                codeId = elem.codeId
                companyId = elem.companyId
            }
        }
    }
}