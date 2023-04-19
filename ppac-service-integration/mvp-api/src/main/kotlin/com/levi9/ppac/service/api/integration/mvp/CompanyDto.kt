package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import nonapi.io.github.classgraph.json.Id

@Schema(description = "Model for a company.")
@JsonRootName("Company")
class CompanyDto {

    @field:Id
    @JMap
    var id: UUID = UUID.randomUUID()

    @field:Schema(
        description = "Name of the company",
        example = "Levi9",
        type = "String",
        minLength = 2,
        maxLength = 150,
        nullable = false
    )

    @field:NotNull
    @field:Size(min = 2, max = 150, message = "Invalid length for name field.")
    @JMap
    var name: String = ""

    @field:Schema(
        description = "Logo of the company",
        example = "[10,20,30,40]",
        type = "ByteArray",
        nullable = true
    )
    @JMap
    var logo: ByteArray? = null

    @field:Schema(
        description = "Description of the company",
        example = """Levi9 is a nearshore technology service provider with around 1000 employees and 50+ customers.
             We specialize in custom made business IT – 95% of our work is on the revenue side of our customers.""",
        type = "String",
        minLength = 20,
        maxLength = 3000,
        nullable = true
    )

    @field:NotNull
    @field:Size(min = 20, max = 3000, message = "Invalid length for description field.")
    @JMap
    var description: String? = null

    @field:Schema(
        description = "Email of the company",
        example = "info@levi9.com",
        type = "String",
        minLength = 5,
        maxLength = 50,
        nullable = true
    )

    @field:NotNull
    @field:Size(min = 5, max = 50, message = "Invalid length for email field.")
    @field:Email(message = "The company email should be a valid one.")
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
