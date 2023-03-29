package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import com.levi9.ppac.service.api.enums.PeriodType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.UUID
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotNull

@Schema(description = "Model for an opening.")
@JsonRootName("Opening")
class OpeningDto {
    @JMap
    var id: UUID = UUID.randomUUID()

    @field:Schema(
        description = "The keywords",
        example = "[\"java\", \"c++\"]",
        type = "List<String>",
        nullable = false
    )
    @JMap
    lateinit var keyWords: List<String>

    @field:Schema(
        description = "The custom keywords",
        example = "[\"java\", \"c++\"]",
        type = "List<String>",
        nullable = false
    )
    @JMap
    lateinit var customKeyWords: List<String>

    @field:Schema(
        description = "Has technical interview",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @NotNull
    @JMap
    var hasTechnicalInterview: Boolean = false

    @field:Schema(
        description = "Has technical test",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @NotNull
    @JMap
    var hasTechnicalTest: Boolean = false

    @field:Schema(
        description = "The number of period type units",
        example = "10",
        type = "Int",
        nullable = false
    )
    @NotNull
    @JMap
    var periodCount: Int = 0

    @field:Schema(
        description = "The period type",
        example = "WEEKS",
        type = "String",
        defaultValue = "WEEKS",
        nullable = false
    )
    @JMap
    var periodType: PeriodType = PeriodType.WEEKS

    @field:Schema(
        description = "The number of opened positions",
        example = "10",
        type = "Int",
        nullable = false
    )
    @NotNull
    @JMap
    var openPositions: Int = 0

    @field:Schema(
        description = "Accept on closing opportunity",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @NotNull
    @JMap
    var acceptOnClosingOpportunity: Boolean = false

    @field:Schema(
        description = "Sign agreement",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @NotNull
    @JMap
    var signAgreement: Boolean = false

    @field:Schema(
        description = "List of trainers",
        type = "List<Trainer>",
        nullable = false
    )
    @JMap
    lateinit var trainers: List<TrainerDto>

    @field:Schema(
        description = "The availability of the opening",
        example = "true",
        type = "Boolean",
        defaultValue = "true",
        nullable = false
    )
    @JMap
    var available: Boolean = true

    @field:Schema(
            description = "The title of the opened position",
            example = "Java Developer",
            type = "String",
            nullable = true
    )
    @JMap
    var title: String? = null

    @field:Schema(
            description = "The description of the opened position",
            example = "This is the description of our new opened position.",
            type = "String",
            nullable = true
    )
    @JMap
    var description: String? = null

    @field:Schema(
            description = "The requirements for the opened position",
            example = "For this position a student should have good knowledge of Java language.",
            type = "String",
            nullable = true
    )
    @JMap
    var requirements: String? = null

    @field:Schema(
            description = "The restrictions for the opened position",
            example = "For this position the restrictions are...",
            type = "String",
            nullable = true
    )
    @JMap
    var restrictions: String? = null

    @field:Schema(
            description = "The recruitment process for the opened position",
            example = "For this position the recruitment process consists of...",
            type = "String",
            nullable = true
    )
    @JMap
    var recruitmentProcess: String? = null

    @field:Schema(
            description = "The start date of the opened position",
            example = "21-07-2023",
            type = "String",
            nullable = true
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "Date should be from future or present")
    @JMap
    var startDate: LocalDate? = null
}
