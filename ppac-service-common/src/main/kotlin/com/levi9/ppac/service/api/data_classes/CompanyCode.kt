package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.*

class CompanyCode (
    var code: UUID )
{
    lateinit var companyId : UUID

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            return CompanyCode(elem.code).apply {
                companyId = elem.companyId
            }
        }
    }
}