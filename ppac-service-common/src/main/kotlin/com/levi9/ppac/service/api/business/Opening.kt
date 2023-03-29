package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.enums.PeriodType
import java.time.LocalDate
import java.util.UUID

class Opening {
    var id: UUID = UUID.randomUUID()
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

    companion object {

        fun parse(elem: OpeningEntity): Opening {
            return Opening().apply {
                id = elem.id
                keyWords = elem.keyWords
                customKeyWords = elem.customKeyWords
                hasTechnicalInterview = elem.hasTechnicalInterview
                hasTechnicalTest = elem.hasTechnicalTest
                periodCount = elem.periodCount
                periodType = elem.periodType
                openPositions = elem.openPositions
                acceptOnClosingOpportunity = elem.acceptOnClosingOpportunity
                trainers = elem.trainers.map { Trainer.parse(it) }
                available = elem.available
                signAgreement = elem.signAgreement
                title = elem.title
                description = elem.description
                requirements = elem.requirements
                restrictions = elem.restrictions
                recruitmentProcess = elem.recruitmentProcess
                startDate = elem.startDate
            }
        }

        fun parse(elem: Opening): OpeningEntity {
            return OpeningEntity(
                    elem.id,
                    elem.keyWords,
                    elem.customKeyWords,
                    elem.hasTechnicalInterview,
                    elem.hasTechnicalTest,
                    elem.periodCount,
                    elem.periodType,
                    elem.openPositions,
                    elem.acceptOnClosingOpportunity,
                    elem.signAgreement,
                    elem.trainers.map { Trainer.parse(it) },
                    elem.available,
            ).apply {
                title = elem.title
                description = elem.description
                requirements = elem.requirements
                restrictions = elem.restrictions
                recruitmentProcess = elem.recruitmentProcess
                startDate = elem.startDate
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Opening) return false

        if (id != other.id) return false
        if (keyWords != other.keyWords) return false
        if (customKeyWords != other.customKeyWords) return false
        if (hasTechnicalInterview != other.hasTechnicalInterview) return false
        if (hasTechnicalTest != other.hasTechnicalTest) return false
        if (periodCount != other.periodCount) return false
        if (periodType != other.periodType) return false
        if (openPositions != other.openPositions) return false
        if (acceptOnClosingOpportunity != other.acceptOnClosingOpportunity) return false
        if (signAgreement != other.signAgreement) return false
        if (trainers != other.trainers) return false
        if (available != other.available) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (requirements != other.requirements) return false
        if (restrictions != other.restrictions) return false
        if (recruitmentProcess != other.recruitmentProcess) return false
        if (startDate != other.startDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + keyWords.hashCode()
        result = 31 * result + customKeyWords.hashCode()
        result = 31 * result + hasTechnicalInterview.hashCode()
        result = 31 * result + hasTechnicalTest.hashCode()
        result = 31 * result + periodCount
        result = 31 * result + periodType.hashCode()
        result = 31 * result + openPositions
        result = 31 * result + acceptOnClosingOpportunity.hashCode()
        result = 31 * result + signAgreement.hashCode()
        result = 31 * result + trainers.hashCode()
        result = 31 * result + available.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (requirements?.hashCode() ?: 0)
        result = 31 * result + (restrictions?.hashCode() ?: 0)
        result = 31 * result + (recruitmentProcess?.hashCode() ?: 0)
        result = 31 * result + (startDate?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Opening(id=$id, keyWords=$keyWords, customKeyWords=$customKeyWords, hasTechnicalInterview=$hasTechnicalInterview, hasTechnicalTest=$hasTechnicalTest, periodCount=$periodCount, periodType=$periodType, openPositions=$openPositions, acceptOnClosingOpportunity=$acceptOnClosingOpportunity, signAgreement=$signAgreement, trainers=$trainers, available=$available, title=$title, description=$description, requirements=$requirements, restrictions=$restrictions, recruitmentProcess=$recruitmentProcess, startDate=$startDate)"
    }
}
