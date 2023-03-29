package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Model for a company code.")
@JsonRootName("CompanyCode")
class CompanyCodeDto {
    @field:Schema(
        description = "Access code of the company",
        nullable = false
    )
    @JMap
    lateinit var accessCode: AccessCodeDto

    @field:Schema(
        description = "The company",
        nullable = false
    )
    @JMap
    lateinit var company: CompanyDto
}
