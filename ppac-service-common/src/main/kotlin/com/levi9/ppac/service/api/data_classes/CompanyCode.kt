package com.levi9.ppac.service.api.data_classes

import com.fasterxml.jackson.annotation.JsonIgnore
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@Schema(description = "Model for a company code.")
data class CompanyCode(

    @JsonIgnore
    val id: UUID,

    @field:Schema(
        description = "Access code of the company",
        nullable = false
    )
    val accessCode: AccessCode,

    @field:Schema(
        description = "The company",
        nullable = false
    )
    val company: Company
) {

    companion object {
        fun parse(elem: CompanyCodeEntity): CompanyCode {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(elem)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return CompanyCode(elem.id, AccessCode.parse(elem.accessCode), Company.parse(elem.company))
        }

        fun parse(elem: CompanyCode): CompanyCodeEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(elem)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            return CompanyCodeEntity(elem.id, AccessCode.parse(elem.accessCode), Company.parse(elem.company))
        }
    }
}
