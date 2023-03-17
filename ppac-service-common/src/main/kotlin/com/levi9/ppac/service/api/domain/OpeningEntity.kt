package com.levi9.ppac.service.api.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.levi9.ppac.service.api.enums.PeriodType
import net.minidev.json.annotate.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "OPENINGS")
data class OpeningEntity(

        @Id
        @Column(name = "ID")
        var id: UUID,

        @ElementCollection
        @CollectionTable(name = "KEYWORDS", joinColumns = [JoinColumn(name = "id")])
        @Column(name = "KEYWORDS")
        var keyWords: List<String>,

        @ElementCollection
        @CollectionTable(name = "CUSTOM_KEYWORDS", joinColumns = [JoinColumn(name = "id")])
        @Column(name = "CUSTOM_KEYWORDS")
        var customKeyWords: List<String>,

        @Column(name = "HAS_TECHNICAL_INTERVIEW", nullable = false)
        var hasTechnicalInterview: Boolean,

        @Column(name = "HAS_TECHNICAL_TEST", nullable = false)
        var hasTechnicalTest: Boolean,

        @Column(name = "PERIOD_COUNT", nullable = false)
        var periodCount: Int,

        @Column(name = "PERIOD_TYPE", nullable = false)
        @Enumerated(EnumType.STRING)
        var periodType: PeriodType = PeriodType.WEEKS,

        @Column(name = "OPEN_POSITIONS", nullable = false)
        var openPositions: Int,

        @Column(name = "ACCEPT_ON_CLOSING_OPPORTUNITY", nullable = false)
        var acceptOnClosingOpportunity: Boolean,

        @Column(name = "SIGN_AGREEMENT", nullable = false)
        var signAgreement: Boolean,

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "ID", referencedColumnName = "ID")
        var trainers: List<TrainerEntity>,

        @Column(name = "AVAILABLE", nullable = false)
        var available: Boolean = true,

) {

    @Column(name = "TITLE", nullable = true)
    var title: String? = null

    @Column(name = "DESCRIPTION", nullable = true)
    var description: String? = null

    @Column(name = "REQUIREMENTS", nullable = true)
    var requirements: String? = null

    @Column(name = "RESTRICTIONS", nullable = true)
    var restrictions: String? = null

    @Column(name = "RECRUITMENT_PROCESS", nullable = true)
    var recruitmentProcess: String? = null

    @Column(name = "START_DATE", nullable = true)
    var startDate: String? = null
}