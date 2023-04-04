package com.levi9.ppac.service.api.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.validator.annotations.ValidPeriodType
import java.time.LocalDate
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Entity
@Table(name = "OPENINGS")
data class OpeningEntity(

    @field:Id
    @Column(name = "ID")
    var id: UUID,

    @field:ElementCollection
    var keyWords: List<String>,

    @field:ElementCollection
    var customKeyWords: List<String>,

    @Column(name = "HAS_TECHNICAL_INTERVIEW", nullable = false)
    var hasTechnicalInterview: Boolean,

    @Column(name = "HAS_TECHNICAL_TEST", nullable = false)
    var hasTechnicalTest: Boolean,

    @Column(name = "PERIOD_COUNT", nullable = false)
    @field:Positive
    @field:Max(value = 24, message = "Invalid value for periodCount field.")
    var periodCount: Int,

    @Column(name = "PERIOD_TYPE", nullable = false)
    @field:Enumerated(EnumType.STRING)
    @ValidPeriodType
    var periodType: PeriodType = PeriodType.WEEKS,

    @Column(name = "OPEN_POSITIONS", nullable = false)
    @field:Positive(message = "The number of open positions should be positive.")
    @field:Max(value = 30, message = "The number of open positions should be maximum 30.")
    var openPositions: Int,

    @Column(name = "ACCEPT_ON_CLOSING_OPPORTUNITY", nullable = false)
    var acceptOnClosingOpportunity: Boolean,

    @Column(name = "SIGN_AGREEMENT", nullable = false)
    var signAgreement: Boolean,

    @OneToMany(cascade = [CascadeType.ALL])
    var trainers: List<TrainerEntity>,

    @Column(name = "AVAILABLE", nullable = false)
    var available: Boolean = true,

    ) {

    @Column(name = "TITLE", nullable = true)
    @field:Size(min = 5, max = 30, message = "Invalid length for title field.")
    var title: String? = null

    @Column(name = "DESCRIPTION", nullable = true)
    @field:Size(min = 40, max = 1000, message = "Invalid length for description field.")
    var description: String? = null

    @Column(name = "REQUIREMENTS", nullable = true)
    @field:Size(min = 10, max = 1000, message = "Invalid length for requirements field.")
    var requirements: String? = null

    @Column(name = "RESTRICTIONS", nullable = true)
    @field:Size(min =2, max = 1000, message = "Invalid length for restrictions field.")
    var restrictions: String? = null

    @Column(name = "RECRUITMENT_PROCESS", nullable = true)
    @field:Size(min =10, max = 2000, message = "Invalid length for recruitmentProcess field.")
    var recruitmentProcess: String? = null

    @Column(name = "START_DATE", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @field:FutureOrPresent(message = "Date should be from future or present.")
    var startDate: LocalDate? = null
}
