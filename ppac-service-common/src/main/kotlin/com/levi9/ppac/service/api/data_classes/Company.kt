package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.CompanyEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Schema(description = "Model for a company.")
data class Company(

    val id: UUID,

    @field:Schema(
        description = "Name of the company",
        example = "Levi9",
        type = "String",
        minLength = 2,
        maxLength = 30,
        nullable = false
    )
    @Size(min = 2, max = 30, message = "The name length should have between 2 and 30 characters.")
    var name: String
) {
    @field:Schema(
        description = "Logo of the company",
        type = "ByteArray",
        nullable = true
    )
    var logo: ByteArray? = null

    @field:Schema(
        description = "Description of the company",
        example = "Levi9 is a nearshore technology service provider with around 1000 employees and 50+ customers. We specialize in custom made business IT – 95% of our work is on the revenue side of our customers.",
        type = "String",
        minLength = 2,
        maxLength = 300,
        nullable = true
    )
    @Size(min = 2, max = 300, message = "The description length should have between 2 and 50 characters.")
    var description: String? = null

    @field:Schema(
        description = "Email of the company",
        example = "info@levi9.com",
        type = "String",
        minLength = 2,
        maxLength = 50,
        nullable = true
    )
    @Size(min = 5, max = 50, message = "The email length should have between 5 and 50 characters.")
    @Email(message = "The company email should be a valid one.")
    var email: String? = null

    @field:Schema(
            description = "List of openings",
            type = "List<Opening>",
            nullable = true
    )
    var openings: List<Opening>? = emptyList()


    companion object {
        fun parse(elem: CompanyEntity): Company {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(elem)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return Company(elem.id, elem.name).apply {
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
}
