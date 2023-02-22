package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import java.util.*

class AccessCode(
    val id: UUID = UUID.randomUUID()
) {
    lateinit var value: String

    var type: CodeType? = null

    var companyId: UUID? = null

    companion object {
        fun parse(elem: AccessCodeEntity): AccessCode {
            return AccessCode(elem.id).apply {
                value = elem.value
                type = elem.type
                companyId = elem.companyId
            }
        }

        fun parse(elem: AccessCode): AccessCodeEntity {
            return AccessCodeEntity(elem.id).apply {
                value = elem.value
                type = elem.type
                companyId = elem.companyId
            }
        }
    }
}