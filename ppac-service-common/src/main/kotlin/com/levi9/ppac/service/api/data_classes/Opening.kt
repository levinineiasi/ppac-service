package com.levi9.ppac.service.api.data_classes

import com.fasterxml.jackson.annotation.JsonFormat
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.enums.PeriodType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotNull

@Schema(description = "Model for an opening.")
data class Opening(

    var id: UUID = UUID.randomUUID(),

    @field:Schema(
            description = "The keywords",
            example = "[\"java\", \"c++\"]",
            type = "List<String>",
            nullable = false
    )
    var keyWords: List<String>,

    @field:Schema(
            description = "The custom keywords",
            example = "[\"java\", \"c++\"]",
            type = "List<String>",
            nullable = false
    )
    var customKeyWords: List<String>,

    @field:Schema(
            description = "Has technical interview",
            example = "true",
            type = "Boolean",
            nullable = false
    )
    @NotNull
    var hasTechnicalInterview: Boolean,

    @field:Schema(
            description = "Has technical test",
            example = "true",
            type = "Boolean",
            nullable = false
    )
    @NotNull
    var hasTechnicalTest: Boolean,

    @field:Schema(
            description = "The number of period type units",
            example = "10",
            type = "Int",
            nullable = false
    )
    @NotNull
    var periodCount: Int,

    @field:Schema(
            description = "The period type",
            example = "WEEKS",
            type = "String",
            defaultValue = "WEEKS",
            nullable = false
    )
    var periodType: PeriodType = PeriodType.WEEKS,

    @field:Schema(
            description = "The number of opened positions",
            example = "10",
            type = "Int",
            nullable = false
    )
    @NotNull
    var openPositions: Int,

    @field:Schema(
            description = "Accept on closing opportunity",
            example = "true",
            type = "Boolean",
            nullable = false
    )
    @NotNull
    var acceptOnClosingOpportunity: Boolean,

    @field:Schema(
            description = "Sign agreement",
            example = "true",
            type = "Boolean",
            nullable = false
    )
    @NotNull
    var signAgreement: Boolean,

    @field:Schema(
            description = "List of trainers",
            type = "List<Trainer>",
            nullable = false
    )
    var trainers: List<Trainer>,

    @field:Schema(
            description = "The availability of the opening",
            example = "true",
            type = "Boolean",
            defaultValue = "true",
            nullable = false
    )
    var available: Boolean = true,

    ) {

    @field:Schema(
            description = "The title of the opened position",
            example = "Java Developer",
            type = "String",
            nullable = true
    )
    var title: String? = null

    @field:Schema(
            description = "The description of the opened position",
            example = "This is the description of our new opened position.",
            type = "String",
            nullable = true
    )
    var description: String? = null

    @field:Schema(
            description = "The requirements for the opened position",
            example = "For this position a student should have good knowledge of Java language.",
            type = "String",
            nullable = true
    )
    var requirements: String? = null

    @field:Schema(
            description = "The restrictions for the opened position",
            example = "For this position the restrictions are...",
            type = "String",
            nullable = true
    )
    var restrictions: String? = null

    @field:Schema(
            description = "The recruitment process for the opened position",
            example = "For this position the recruitment process consists of...",
            type = "String",
            nullable = true
    )
    var recruitmentProcess: String? = null

    @field:Schema(
            description = "The start date of the opened position",
            example = "21-07-2023",
            type = "String",
            nullable = true
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "Date should be from future or present")
    var startDate: LocalDate? = null

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

}
