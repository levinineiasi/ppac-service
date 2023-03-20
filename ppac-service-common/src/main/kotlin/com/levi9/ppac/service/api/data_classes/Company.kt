package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Size

@Schema(description = "Model for a company.")
data class Company(
    val id: UUID,

    @field:Schema(
        description = "Name to be displayed of the company",
        example = "Levi9",
        type = "String",
        minLength = 2,
        maxLength = 30,
        nullable = false
    )
    @get:Size(min = 2, max = 30, message = "The displayed name length should have between 2 and 30 characters.")
    var displayName: String
) {

    @field:Schema(
        description = "Full name of the company",
        example = "Levi9 Global Sourcing",
        type = "String",
        minLength = 2,
        maxLength = 50,
        nullable = true
    )
    @get:Size(min = 2, max = 50, message = "The full name length should have between 2 and 50 characters.")
    var fullName: String? = null

    @field:Schema(
        description = "Logo of the company",
        type = "ByteArray",
        nullable = true
    )
    var logo: ByteArray? = null

    companion object {
        fun parse(elem: CompanyEntity): Company {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(elem)
            if (violations.isNotEmpty()) {
//                throw ConstraintViolationException(violations)
            }
            return Company(elem.id,elem.displayName).apply {
                fullName = elem.fullName
                logo = elem.logo
            }
        }

        fun parse(elem: Company): CompanyEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(elem)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return CompanyEntity(elem.id, elem.displayName).apply {
                fullName = elem.fullName
                logo = elem.logo
            }
        }
    }
}
