package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "Model for a trainer.")
@JsonRootName("Trainer")
class TrainerDto {
    @JMap
    var id: UUID = UUID.randomUUID()

    @field:Schema(
        description = "The trainer's name",
        example = "Popescu Ion",
        type = "String",
        nullable = false
    )
    @JMap
    var name: String = ""

    @field:Schema(
        description = "The trainer's description",
        example = "The description",
        type = "String",
        nullable = false
    )
    @JMap
    var description: String = ""

    @field:Schema(
        description = "The trainer's Linkedin url",
        example = "https://www.linkedin.com/in/popescu-ion",
        type = "String",
        nullable = true
    )
    @JMap
    var linkedinURL: String? = null

    @field:Schema(
        description = "The trainer's avatar",
        type = "ByteArray",
        nullable = true
    )
    @JMap
    var avatar: ByteArray? = null
}
