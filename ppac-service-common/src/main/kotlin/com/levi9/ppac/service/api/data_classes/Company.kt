package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyEntity
import java.util.*

class Company {
    var id: UUID = UUID.randomUUID()
    var name: String = ""
    var logo: ByteArray? = null
    var description: String? = null
    var email: String? = null
    var openings: List<Opening>? = emptyList()

    companion object {
        fun parse(elem: CompanyEntity): Company {
            return Company().apply {
                id = elem.id
                name = elem.name
                logo = elem.logo
                description = elem.description
                email = elem.email
                openings = elem.openings.map { Opening.parse(it) }
            }
        }

        fun parse(elem: Company): CompanyEntity {
            return CompanyEntity(elem.id, elem.name).apply {
                logo = elem.logo
                description = elem.description
                email = elem.email
                openings = elem.openings?.map { Opening.parse(it) }?: emptyList()
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Company) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (logo != null) {
            if (other.logo == null) return false
            if (!logo.contentEquals(other.logo)) return false
        } else if (other.logo != null) return false
        if (description != other.description) return false
        if (email != other.email) return false
        if (openings != other.openings) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (logo?.contentHashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (openings?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Company(id=$id, name='$name', logo=${logo?.contentToString()}, description=$description, email=$email, openings=$openings)"
    }
}
