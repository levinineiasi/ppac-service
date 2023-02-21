package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.enums.CodeType
import java.util.*

class Code(
    val id: UUID = UUID.randomUUID()
) {
    lateinit var value: String

    var type: CodeType? = null

    var companyId: UUID? = null

    companion object {
        fun parse(elem: com.levi9.ppac.service.api.domain.Code): Code {
            return Code(elem.id).apply {
                value = elem.value
                type = elem.type
                companyId = elem.companyId
            }
        }

        fun parse(elem: Code): com.levi9.ppac.service.api.domain.Code {
            return com.levi9.ppac.service.api.domain.Code(elem.id).apply {
                value = elem.value
                type = elem.type
                companyId = elem.companyId
            }
        }
    }
}