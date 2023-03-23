package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.TrainerEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Model for a trainer.")
data class Trainer(

    var id: UUID = UUID.randomUUID(),

    @field:Schema(
            description = "The trainer's name",
            example = "Popescu Ion",
            type = "String",
            nullable = false
    )
    var name: String,

    @field:Schema(
            description = "The trainer's description",
            example = "The description",
            type = "String",
            nullable = false
    )
    var description: String

    ) {
    @field:Schema(
            description = "The trainer's Linkedin url",
            example = "https://www.linkedin.com/in/popescu-ion",
            type = "String",
            nullable = true
    )
    var linkedinURL: String? = null

    @field:Schema(
            description = "The trainer's avatar",
            type = "String",
            nullable = true
    )
    var avatar: String? = null

    companion object {
        fun parse(elem: TrainerEntity): Trainer {
            return Trainer(
                elem.id,
                elem.name,
                elem.description,
            ).apply {
                linkedinURL = elem.linkedinURL
                avatar = elem.avatar
            }
        }

        fun parse(elem: Trainer): TrainerEntity {
            return TrainerEntity(
                elem.id,
                elem.name,
                elem.description,
            ).apply {
                linkedinURL = elem.linkedinURL
                avatar = elem.avatar
            }
        }
    }
}