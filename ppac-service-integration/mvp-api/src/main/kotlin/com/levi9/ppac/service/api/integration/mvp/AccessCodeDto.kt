package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.validator.ValidCodeType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Schema(description = "Model for a access code.")
@JsonRootName("AccessCode")
class AccessCodeDto {
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
    @JMap
    var value: Int = 100000

    @NotNull
    @JMap
    lateinit var id: UUID

    @field:Schema(
        description = "Type of access code",
        example = "COMPANY_CODE",
        type = "String",
        defaultValue = "COMPANY_CODE",
        nullable = true
        )
    @ValidCodeType
    @JMap
    var type: CodeType? = CodeType.COMPANY_CODE
}
