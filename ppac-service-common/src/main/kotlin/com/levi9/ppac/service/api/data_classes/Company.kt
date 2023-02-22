package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyEntity
import java.util.*

class Company(
    val id: UUID = UUID.randomUUID()
) {
    var displayName: String? = null

    var fullName: String? = null

    var logo: ByteArray? = null

    companion object {
        fun parse(elem: CompanyEntity): Company {
            return Company(elem.id).apply {
                displayName = elem.displayName
                fullName = elem.fullName
                logo = elem.logo
            }
        }

        fun parse(elem: Company): CompanyEntity {
            return CompanyEntity(elem.id).apply {
                displayName = elem.displayName
                fullName = elem.fullName
                logo = elem.logo
            }
        }
    }
}