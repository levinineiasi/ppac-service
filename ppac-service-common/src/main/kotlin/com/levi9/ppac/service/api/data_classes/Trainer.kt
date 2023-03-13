package com.levi9.ppac.service.api.data_classes

import com.levi9.ppac.service.api.domain.TrainerEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Model for a trainer.")
data class Trainer(

        var id: UUID,

        var name: String,

        var description: String,

        var linkedinURL: String,

        ) {

    var avatar: String? = null

    companion object {
        fun parse(elem: TrainerEntity): Trainer {
            return Trainer(
                    elem.id,
                    elem.name,
                    elem.description,
                    elem.linkedinURL
            ).apply {
                avatar = elem.avatar
            }
        }

        fun parse(elem: Trainer): TrainerEntity {
            return TrainerEntity(
                    elem.id,
                    elem.name,
                    elem.description,
                    elem.linkedinURL
            ).apply {
                avatar = elem.avatar
            }
        }
    }
}