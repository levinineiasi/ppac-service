package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Schema(description = "Model for a company.")
@JsonRootName("Company")
class CompanyDto {
    @JMap
    lateinit var id: UUID

    @field:Schema(
        description = "Name of the company",
        example = "Levi9",
        type = "String",
        minLength = 2,
        maxLength = 30,
        nullable = false
    )
    @Size(min = 2, max = 30, message = "The name length should have between 2 and 30 characters.")
    @JMap
    var name: String = ""

    @field:Schema(
        description = "Logo of the company",
        type = "ByteArray",
        nullable = true
    )
    @JMap
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
    @JMap
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
    @JMap
    var email: String? = null

    @field:Schema(
            description = "List of openings",
            type = "List<Opening>",
            nullable = true
    )
    @JMap
    var openings: List<OpeningDto>? = emptyList()
}
