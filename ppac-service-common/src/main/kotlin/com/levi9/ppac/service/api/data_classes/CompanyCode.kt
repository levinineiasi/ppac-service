package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.*

class CompanyCode (
    var code: UUID, var id: UUID )
{
    lateinit var companyId : UUID

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            return CompanyCode(elem.id, elem.codeId.id).apply {
            }
        }
    }
}