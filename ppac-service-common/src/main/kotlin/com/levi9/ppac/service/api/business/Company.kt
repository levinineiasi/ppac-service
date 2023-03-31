package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.CompanyEntity
import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class Company(
    var id: UUID
) {
    @field:Size(min = 2, max = 30, message = "The name length should have between 2 and 30 characters.")
    var name: String = ""
    var logo: ByteArray? = null

    @field:Size(min = 2, max = 300, message = "The description length should have between 2 and 300 characters.")
    var description: String? = null

    @field:Size(min = 5, max = 50, message = "The email length should have between 5 and 50 characters.")
    @field:Email(message = "The company email should be a valid one.")
    var email: String? = null
    var openings: List<Opening>? = emptyList()

    companion object ConverterImpl : Converter<Company, CompanyEntity> {
        override fun toBusinessModel(entityObject: CompanyEntity): Company {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(entityObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return Company(entityObject.id).apply {
                name = entityObject.name
                logo = entityObject.logo
                description = entityObject.description
                email = entityObject.email
                openings = entityObject.openings.map { Opening.toBusinessModel(it) }
            }
        }

        override fun toEntity(businessObject: Company): CompanyEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return CompanyEntity(businessObject.id, businessObject.name).apply {
                logo = businessObject.logo
                description = businessObject.description
                email = businessObject.email
                openings = businessObject.openings?.map { Opening.toEntity(it) } ?: emptyList()
            }
        }
    }

    override fun toString(): String {
        return "Company(id=$id, name='$name', logo=${logo?.contentToString()}, description=$description, email=$email, openings=$openings)"
    }

    constructor() : this(UUID.randomUUID())
}
