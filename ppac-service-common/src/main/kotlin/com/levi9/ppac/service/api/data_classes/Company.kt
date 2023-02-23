package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyEntity
import java.util.*
import javax.validation.constraints.Size

class Company(
    val id: UUID = UUID.randomUUID()
) {
    @Size(min = 2, max = 30, message = "The displayed name length should have between 2 and 30 characters.")
    lateinit var displayName: String

    @Size(min = 2, max = 50, message = "The full name length should have between 2 and 50 characters.")
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