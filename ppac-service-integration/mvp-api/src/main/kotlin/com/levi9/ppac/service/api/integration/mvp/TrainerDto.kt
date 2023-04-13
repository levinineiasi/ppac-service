package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import nonapi.io.github.classgraph.json.Id

@Schema(description = "Model for a trainer.")
@JsonRootName("Trainer")
class TrainerDto {

    @field:Id
    @JMap
    var id: UUID = UUID.randomUUID()

    @field:Schema(
        description = "The trainer's name",
        example = "Popescu Ion",
        type = "String",
        nullable = false
    )
    @field:NotNull
    @field:Size(min = 2, max = 30, message = "Invalid length for name field.")
    @JMap
    var name: String = ""

    @field:Schema(
        description = "The trainer's description",
        example = "Is a tech expert who specializes in areas such as software development, network administration and cybersecurity.",
        type = "String",
        nullable = false
    )

    @field:NotNull
    @field:Size(min = 40, max = 1000, message = "Invalid length for description field.")
    @JMap
    var description: String = ""

    @field:Schema(
        description = "The trainer's Linkedin url",
        example = "https://www.linkedin.com/in/popescu-ion",
        type = "String",
        nullable = true
    )

    @field:Size(min = 20, max = 100, message = "Invalid size Linkedin URL")
    @JMap
    var linkedinURL: String? = null

    @field:Schema(
        description = "The trainer's avatar",
        example = "[10,20,30,40]",
        type = "ByteArray",
        nullable = true
    )
    @JMap
    var avatar: ByteArray? = null
}
