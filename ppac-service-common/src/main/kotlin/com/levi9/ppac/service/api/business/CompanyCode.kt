package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.UUID

class CompanyCode(
    val id: UUID,
    val accessCode: AccessCode,
    val company: Company
) {

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            return CompanyCode(elem.id, AccessCode.parse(elem.accessCode), Company.parse(elem.company))
        }

        fun parse(elem: CompanyCode): CompanyCodeEntity {
            return CompanyCodeEntity(elem.id, AccessCode.parse(elem.accessCode), Company.parse(elem.company))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompanyCode) return false

        if (id != other.id) return false
        if (accessCode != other.accessCode) return false
        if (company != other.company) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + accessCode.hashCode()
        result = 31 * result + company.hashCode()
        return result
    }

    override fun toString(): String {
        return "CompanyCode(id=$id, accessCode=$accessCode, company=$company)"
    }
}
