package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.validator.ValidCodeType
import java.util.UUID
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class AccessCode(
    var id: UUID

) {
    @NotNull
    @Min(value = 100000, message = "Value should have minimum 6 characters.")
    @Max(value = 999999, message = "Value should have maximum 6 characters.")
    var value: Int = 0

    @Enumerated(EnumType.STRING)
    @ValidCodeType
    var type: CodeType = CodeType.COMPANY_CODE

    companion object ConverterImpl : Converter<AccessCode, AccessCodeEntity> {
        override fun toEntity(businessObject: AccessCode): AccessCodeEntity {
            return AccessCodeEntity(businessObject.id, businessObject.value, businessObject.type)
        }

        override fun toBusinessModel(entityObject: AccessCodeEntity): AccessCode {
            return AccessCode(entityObject.id).apply {
                value = entityObject.value
                type = entityObject.type
            }
        }
    }

    override fun toString(): String {
        return "AccessCode(id=$id, value=$value, type=$type)"
    }

    constructor() : this(UUID.randomUUID())

}
