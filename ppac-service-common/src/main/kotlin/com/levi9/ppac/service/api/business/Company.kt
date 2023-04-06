package com.levi9.ppac.service.api.business

import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.CompanyEntity
import java.util.UUID
import javax.persistence.Id
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JGlobalMap
data class Company(

    @field:NotNull
    @field:Id
    var id: UUID,

    @field:NotNull
    @field:Size(min = 2, max = 30, message = "The name should have between 2 and 30 characters length.")
    var name: String
) {
    var logo: ByteArray? = null

    @field:Size(min = 40, max = 1000, message = "The description should have between 2 and 300 characters length.")
    var description: String? = null

    @field:Size(min = 5, max = 50, message = "The email should have between 5 and 50 characters length.")
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
            val codesEntityToBusinessModelMapper: JMapper<Company, CompanyEntity> =
                JMapper(Company::class.java, CompanyEntity::class.java)
            return codesEntityToBusinessModelMapper.getDestination(entityObject)
        }

        override fun toEntity(businessObject: Company): CompanyEntity {

            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val codesBusinessModelToEntityMapper: JMapper<CompanyEntity, Company> =
                JMapper(CompanyEntity::class.java, Company::class.java)
            return codesBusinessModelToEntityMapper.getDestination(businessObject)
        }
    }

    override fun toString(): String {
        return """Company(
            id=$id,
            name='$name',
            logo=${logo?.contentToString()},
            description=$description,
            email=$email,
            openings=$openings)""".trimMargin()
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID(), "")
}
