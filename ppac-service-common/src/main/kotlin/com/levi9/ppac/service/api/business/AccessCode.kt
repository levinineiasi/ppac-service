package com.levi9.ppac.service.api.business

import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.validator.annotations.ValidCodeType
import java.util.UUID
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@JGlobalMap
data class AccessCode(

    var id: UUID,

    @field:NotNull
    @field:Min(value = 100000, message = "Value should have minimum 6 characters.")
    @field:Max(value = 999999, message = "Value should have maximum 6 characters.")
    var value: Int,

    @field:Enumerated(EnumType.STRING)
    @field:ValidCodeType
    var type: CodeType = CodeType.COMPANY_CODE
) {
    companion object ConverterImpl : Converter<AccessCode, AccessCodeEntity> {
        override fun toEntity(businessObject: AccessCode): AccessCodeEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val codesBusinessModelToEntityMapper: JMapper<AccessCodeEntity, AccessCode> =
                JMapper(AccessCodeEntity::class.java, AccessCode::class.java)
            return codesBusinessModelToEntityMapper.getDestination(businessObject)
        }

        override fun toBusinessModel(entityObject: AccessCodeEntity): AccessCode {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(entityObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val codesEntityToBusinessModelMapper: JMapper<AccessCode, AccessCodeEntity> =
                JMapper(AccessCode::class.java, AccessCodeEntity::class.java)
            return codesEntityToBusinessModelMapper.getDestination(entityObject)
        }
    }

    override fun toString(): String {
        return "AccessCode(id=$id, value=$value, type=$type)"
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID(), Int.MIN_VALUE)

}
