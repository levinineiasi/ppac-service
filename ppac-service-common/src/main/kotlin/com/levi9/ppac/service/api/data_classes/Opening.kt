package com.levi9.ppac.service.api.data_classes

import com.fasterxml.jackson.annotation.JsonIgnore
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.domain.TrainerEntity
import com.levi9.ppac.service.api.enums.PeriodType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*


@Schema(description = "Model for an opening.")
data class Opening(

    val id: UUID,

    var keyWords: List<String>,

    var customKeyWords: List<String>,

    var hasTechnicalInterview: Boolean,

    var hasTechnicalTest: Boolean,

    var periodCount: Int,

    var periodType: PeriodType = PeriodType.WEEKS,

    var openPositions: Int,

    var acceptOnClosingOpportunity: Boolean,

    var signAgreement: Boolean,

    var trainers: List<TrainerEntity>,

    var available: Boolean = true,
) {
    var title: String? = null

    var description: String? = null

    var requirements: String? = null

    var restrictions: String? = null

    var recruitmentProcess: String? = null

    var startDate: String? = null

    companion object {

        fun parse(elem: OpeningEntity): Opening {
            return Opening(
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
                elem.trainers,
                elem.available
            ).apply {
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
                elem.trainers,
                elem.available
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
}