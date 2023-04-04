package com.levi9.ppac.service.api.business

import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.TrainerEntity
import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@JGlobalMap
class Trainer(
    var id: UUID
) {
    var name: String = ""
    var description: String = ""
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
        return "Trainer(id=$id, name='$name', description='$description', linkedinURL=$linkedinURL, avatar=$avatar)"
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID())
}
