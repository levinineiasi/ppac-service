package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.TrainerEntity
import java.util.UUID

class Trainer(
    var id: UUID
) {
    var name: String = ""
    var description: String = ""
    var linkedinURL: String? = null
    var avatar: ByteArray? = null

    companion object ConverterImpl : Converter<Trainer, TrainerEntity> {
        override fun toBusinessModel(entityObject: TrainerEntity): Trainer {
            return Trainer(entityObject.id).apply {
                name = entityObject.name
                description = entityObject.description
                linkedinURL = entityObject.linkedinURL
                avatar = entityObject.avatar
            }
        }

        override fun toEntity(businessObject: Trainer): TrainerEntity {
            return TrainerEntity(
                businessObject.id,
                businessObject.name,
                businessObject.description,
            ).apply {
                linkedinURL = businessObject.linkedinURL
                avatar = businessObject.avatar
            }
        }
    }

    override fun toString(): String {
        return "Trainer(id=$id, name='$name', description='$description', linkedinURL=$linkedinURL, avatar=$avatar)"
    }

    constructor() : this(UUID.randomUUID())
}
