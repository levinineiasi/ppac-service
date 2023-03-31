package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.enums.PeriodType
import java.time.LocalDate
import java.util.UUID

data class Opening(
    var id: UUID
) {
    var keyWords: List<String> = mutableListOf()
    var customKeyWords: List<String> = mutableListOf()
    var hasTechnicalInterview: Boolean = false
    var hasTechnicalTest: Boolean = false
    var periodCount: Int = 0
    var periodType: PeriodType = PeriodType.WEEKS
    var openPositions: Int = 0
    var acceptOnClosingOpportunity: Boolean = false
    var signAgreement: Boolean = false
    var trainers: List<Trainer> = mutableListOf()
    var available: Boolean = true
    var title: String? = null
    var description: String? = null
    var requirements: String? = null
    var restrictions: String? = null
    var recruitmentProcess: String? = null
    var startDate: LocalDate? = null

    companion object ConverterImpl : Converter<Opening, OpeningEntity> {

        override fun toBusinessModel(entityObject: OpeningEntity): Opening {
            return Opening(entityObject.id).apply {
                keyWords = entityObject.keyWords
                customKeyWords = entityObject.customKeyWords
                hasTechnicalInterview = entityObject.hasTechnicalInterview
                hasTechnicalTest = entityObject.hasTechnicalTest
                periodCount = entityObject.periodCount
                periodType = entityObject.periodType
                openPositions = entityObject.openPositions
                acceptOnClosingOpportunity = entityObject.acceptOnClosingOpportunity
                trainers = entityObject.trainers.map { Trainer.toBusinessModel(it) }
                available = entityObject.available
                signAgreement = entityObject.signAgreement
                title = entityObject.title
                description = entityObject.description
                requirements = entityObject.requirements
                restrictions = entityObject.restrictions
                recruitmentProcess = entityObject.recruitmentProcess
                startDate = entityObject.startDate
            }
        }

        override fun toEntity(businessObject: Opening): OpeningEntity {
            return OpeningEntity(
                businessObject.id,
                businessObject.keyWords,
                businessObject.customKeyWords,
                businessObject.hasTechnicalInterview,
                businessObject.hasTechnicalTest,
                businessObject.periodCount,
                businessObject.periodType,
                businessObject.openPositions,
                businessObject.acceptOnClosingOpportunity,
                businessObject.signAgreement,
                businessObject.trainers.map { Trainer.toEntity(it) },
                businessObject.available,
            ).apply {
                title = businessObject.title
                description = businessObject.description
                requirements = businessObject.requirements
                restrictions = businessObject.restrictions
                recruitmentProcess = businessObject.recruitmentProcess
                startDate = businessObject.startDate
            }
        }
    }

    override fun toString(): String {
        return "Opening(id=$id, keyWords=$keyWords, customKeyWords=$customKeyWords, hasTechnicalInterview=$hasTechnicalInterview, hasTechnicalTest=$hasTechnicalTest, periodCount=$periodCount, periodType=$periodType, openPositions=$openPositions, acceptOnClosingOpportunity=$acceptOnClosingOpportunity, signAgreement=$signAgreement, trainers=$trainers, available=$available, title=$title, description=$description, requirements=$requirements, restrictions=$restrictions, recruitmentProcess=$recruitmentProcess, startDate=$startDate)"
    }

    constructor() : this(UUID.randomUUID())
}
