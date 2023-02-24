package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.*

class CompanyCode(
    val id: UUID,

    val accessCode: AccessCode,

    val company: Company
) {

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            return CompanyCode(elem.id, AccessCode.parse(elem.accessCode), Company.parse(elem.company))
        }
    }
}