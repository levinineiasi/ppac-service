package com.levi9.ppac.service.api.integration.mvp

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.googlecode.jmapper.annotations.JMap
import com.levi9.ppac.service.api.enums.PeriodType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.UUID
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size
import nonapi.io.github.classgraph.json.Id

@Schema(description = "Model for an opening.")
@JsonRootName("Opening")
class OpeningDto {

    @field:Id
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
        example = "[\"java-react\", \"unity-c#\"]",
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
    @field:NotNull
    @JMap
    var hasTechnicalInterview: Boolean = false

    @field:Schema(
        description = "Has technical test",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @field:NotNull
    @JMap
    var hasTechnicalTest: Boolean = false

    @field:Schema(
        description = "The number of period type units",
        example = "10",
        type = "Int",
        nullable = false
    )
    @field:NotNull
    @field:Positive(message = "PeriodCount should be positive.")
    @field:Max(value = 50, message = "Invalid value for periodCount field.")
    @JMap
    var periodCount: Int = 1

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
        example = "14",
        type = "Int",
        nullable = false
    )
    @field:NotNull
    @field:Positive(message = "The number of open positions should be positive.")
    @field:Max(value = 50, message = "The number of open positions should be maximum 30.")
    @JMap
    var openPositions: Int = 1

    @field:Schema(
        description = "Accept on closing opportunity",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @field:NotNull
    @JMap
    var acceptOnClosingOpportunity: Boolean = false

    @field:Schema(
        description = "Sign agreement",
        example = "true",
        type = "Boolean",
        nullable = false
    )
    @field:NotNull
    @JMap
    var signAgreement: Boolean = false

    @field:Schema(
        description = "List of trainers",
        type = "List<Trainer>",
        nullable = false
    )
    @JMap
    var trainers: List<TrainerDto> = emptyList()

    @field:Schema(
        description = "The availability of the opening",
        example = "true",
        type = "Boolean",
        defaultValue = "true",
        nullable = false
    )

    @field:NotNull
    @JMap
    var available: Boolean = true

    @field:Schema(
        description = "The title of the opened position",
        example = "Java Developer",
        type = "String",
        nullable = true
    )

    @field:Size(min = 5, max = 150, message = "Invalid length for title field.")
    @JMap
    var title: String? = null

    @field:Schema(
        description = "The description of the opened position",
        example = "This is the description of our new opened position.",
        type = "String",
        minLength = 20,
        maxLength = 3000,
        nullable = true
    )

    @field:Size(min = 20, max = 3000, message = "Invalid length for description field.")
    @JMap
    var description: String? = null

    @field:Schema(
        description = "The requirements for the opened position",
        example = "For this position a student should have good knowledge of Java language.",
        type = "String",
        maxLength = 3000,
        nullable = true
    )

    @field:Size(max = 3000, message = "Invalid length for requirements field.")
    @JMap
    var requirements: String? = null

    @field:Schema(
        description = "The restrictions for the opened position",
        example = "For this position the restrictions are...",
        type = "String",
        maxLength = 3000,
        nullable = true
    )

    @field:Size(max = 3000, message = "Invalid length for restrictions field.")
    @JMap
    var restrictions: String? = null

    @field:Schema(
        description = "The recruitment process for the opened position",
        example = "For this position the recruitment process consists of...",
        type = "String",
        maxLength = 3000,
        nullable = true
    )

    @field:Size(max = 3000, message = "Invalid length for recruitmentProcess field.")
    @JMap
    var recruitmentProcess: String? = null

    @field:Schema(
        description = "The start date of the opened position",
        example = "2023-07-21",
        type = "LocalDate",
        nullable = true
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    @field:FutureOrPresent(message = "Date should be from future or present")
    @JMap
    var startDate: LocalDate? = null

    @field:Schema(
            description = "The number of views of this opening",
            example = "10",
            type = "Int",
            nullable = false
    )
    @field:NotNull
    @field:Min(value = 0, message = "The number of views should be greater or equal to 0.")
    @JMap
    var views: Int = 0

}
