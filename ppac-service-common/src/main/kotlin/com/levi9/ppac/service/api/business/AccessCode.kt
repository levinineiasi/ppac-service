package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import java.util.*

class AccessCode {
    var id: UUID = UUID.randomUUID()
    var value: Int = 0
    var type: CodeType? = CodeType.COMPANY_CODE

    companion object {
        fun parse(elem: AccessCodeEntity): AccessCode {
            return AccessCode().apply {
                id = elem.id
                value = elem.value
                type = elem.type
            }
        }

        fun parse(elem: AccessCode): AccessCodeEntity {
            return AccessCodeEntity(elem.id, elem.value).apply {
                type = elem.type
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AccessCode) return false

        if (id != other.id) return false
        if (value != other.value) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + value
        result = 31 * result + (type?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "AccessCode(id=$id, value=$value, type=$type)"
    }
}
