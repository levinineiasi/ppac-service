package com.levi9.ppac.service.api.business

import com.fasterxml.jackson.annotation.JsonFormat
import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.enums.PeriodType
import java.time.LocalDate
import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@JGlobalMap
data class Opening(
    var id: UUID,
    var keyWords: List<String> = mutableListOf(),
    var customKeyWords: List<String> = mutableListOf(),
    var hasTechnicalInterview: Boolean = false,
    var hasTechnicalTest: Boolean = false,
    var periodCount: Int = 0,
    var periodType: PeriodType = PeriodType.WEEKS,
    var openPositions: Int = 0,
    var acceptOnClosingOpportunity: Boolean = false,
    var signAgreement: Boolean = false,
    var trainers: List<Trainer> = mutableListOf(),
    var available: Boolean = true
) {
    var title: String? = null
    var description: String? = null
    var requirements: String? = null
    var restrictions: String? = null
    var recruitmentProcess: String? = null

    @JsonFormat(pattern = "dd-MM-yyyy")
    var startDate: LocalDate? = null

    companion object ConverterImpl : Converter<Opening, OpeningEntity> {

        override fun toBusinessModel(entityObject: OpeningEntity): Opening {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(entityObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val openingEntityToBusinessModelMapper: JMapper<Opening, OpeningEntity> =
                JMapper(Opening::class.java, OpeningEntity::class.java)
            return openingEntityToBusinessModelMapper.getDestination(entityObject)
        }

        override fun toEntity(businessObject: Opening): OpeningEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val openingBusinessModelToEntityMapper: JMapper<OpeningEntity, Opening> =
                JMapper(OpeningEntity::class.java, Opening::class.java)
            return openingBusinessModelToEntityMapper.getDestination(businessObject)
        }
    }

    override fun toString(): String {
        return """
            Opening(
                id=$id,
                keyWords=$keyWords,
                customKeyWords=$customKeyWords,
                hasTechnicalInterview=$hasTechnicalInterview,
                hasTechnicalTest=$hasTechnicalTest,
                periodCount=$periodCount,
                periodType=$periodType,
                openPositions=$openPositions,
                acceptOnClosingOpportunity=$acceptOnClosingOpportunity,
                signAgreement=$signAgreement,
                trainers=$trainers,
                available=$available,
                title=$title,
                description=$description,
                requirements=$requirements,
                restrictions=$restrictions,
                recruitmentProcess=$recruitmentProcess,
                startDate=$startDate)""".trimIndent()
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID())
}
