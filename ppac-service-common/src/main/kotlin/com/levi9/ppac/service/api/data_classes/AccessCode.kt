package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.validator.ValidCodeType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Schema(description = "Model for a access code.")
class AccessCode(

    @NotNull
    val id: UUID,

    @field:Schema(
        description = "The access code",
        example = "567324",
        type = "Int",
        minimum = "100000",
        maximum = "999999",
        nullable = false
    )
    @NotNull
    @Min(value = 100000, message = "Value should have minimum 6 characters.")
    @Max(value = 999999, message = "Value should have maximum 6 characters.")
    var value: Int
) {

    @field:Schema(
        description = "Type of access code",
        example = "COMPANY_CODE",
        type = "String",
        defaultValue = "COMPANY_CODE",
        nullable = true
        )
    @ValidCodeType
    var type: CodeType? = CodeType.COMPANY_CODE

    companion object {
        fun parse(elem: AccessCodeEntity): AccessCode {
            return AccessCode(elem.id, elem.value).apply {
               type = elem.type
            }
        }

        fun parse(elem: AccessCode): AccessCodeEntity {
            return AccessCodeEntity(elem.id, elem.value).apply {
                type = elem.type
            }
        }
    }
}