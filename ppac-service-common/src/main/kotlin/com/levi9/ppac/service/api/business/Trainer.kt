package com.levi9.ppac.service.api.business

import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.TrainerEntity
import java.util.UUID
import javax.persistence.Id
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.Size

@JGlobalMap
data class Trainer(

    @field:Id
    var id: UUID,

    @field:Size(min = 3, max = 50, message = "Invalid length for name field.")
    var name: String = "",

    @field:Size(max = 150, message = "Invalid length for description field.")
    var role: String = ""
) {

    @field:Size(min = 20, max = 100, message = "Invalid size Linkedin URL")
    var linkedinURL: String? = null

    var avatar: ByteArray? = null

    companion object ConverterImpl : Converter<Trainer, TrainerEntity> {
        override fun toBusinessModel(entityObject: TrainerEntity): Trainer {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(entityObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val trainerEntityToBusinessModelMapper: JMapper<Trainer, TrainerEntity> =
                JMapper(Trainer::class.java, TrainerEntity::class.java)
            return trainerEntityToBusinessModelMapper.getDestination(entityObject)
        }

        override fun toEntity(businessObject: Trainer): TrainerEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val trainerBusinessModelToEntityMapper: JMapper<TrainerEntity, Trainer> =
                JMapper(TrainerEntity::class.java, Trainer::class.java)
            return trainerBusinessModelToEntityMapper.getDestination(businessObject)
        }
    }

    override fun toString(): String {
        return "Trainer(" +
                "id=$id," +
                " name='$name'," +
                " role='$role'," +
                " linkedinURL=$linkedinURL," +
                " avatar=$avatar)"
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID())
}
