package com.levi9.ppac.service.api.business

import com.fasterxml.jackson.annotation.JsonFormat
import com.googlecode.jmapper.JMapper
import com.googlecode.jmapper.annotations.JGlobalMap
import com.googlecode.jmapper.annotations.JMap
import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.CompanyEntity
import com.levi9.ppac.service.api.domain.OpeningEntity
import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.validator.annotations.ValidPeriodType
import java.time.LocalDate
import java.util.UUID
import javax.persistence.ElementCollection
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@JGlobalMap
data class Opening(

    @field:NotNull
    @field:Id
    var id: UUID,

    @field:NotNull
    @field:ElementCollection
    var keyWords: List<String> = mutableListOf(),

    @field:NotNull
    @field:ElementCollection
    var customKeyWords: List<String> = mutableListOf(),

    @field:NotNull
    var hasTechnicalInterview: Boolean = false,

    @field:NotNull
    var hasTechnicalTest: Boolean = false,

    @field:NotNull
    @field:Positive(message = "The number of open positions should be positive.")
    @field:Max(value = 24, message = "Invalid value for periodCount field.")
    var periodCount: Int = 1,

    @field:Enumerated(EnumType.STRING)
    @field:ValidPeriodType
    var periodType: PeriodType = PeriodType.WEEKS,

    @field:NotNull
    @field:Positive(message = "The number of open positions should be positive.")
    @field:Max(value = 30, message = "The number of open positions should be maximum 30.")
    var openPositions: Int = 1,

    @field:NotNull
    var acceptOnClosingOpportunity: Boolean = false,

    @field:NotNull
    var signAgreement: Boolean = false,

    var trainers: List<Trainer> = mutableListOf(),

    @field:NotNull
    var available: Boolean = true,

    var companyId: UUID? = UUID.randomUUID()
) {
    @field:Size(min = 5, max = 30, message = "Invalid length for title field.")
    var title: String? = null

    @field:Size(min = 40, max = 1000, message = "Invalid length for description field.")
    var description: String? = null

    @field:Size(min = 10, max = 1000, message = "Invalid length for requirements field.")
    var requirements: String? = null

    @field:Size(min = 2, max = 1000, message = "Invalid length for restrictions field.")
    var restrictions: String? = null

    @field:Size(min = 10, max = 2000, message = "Invalid length for recruitmentProcess field.")
    var recruitmentProcess: String? = null

    @JsonFormat(pattern = "yyyy-MM-dd")
    @field:FutureOrPresent(message = "Date should be from future or present.")
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
            val op =  openingEntityToBusinessModelMapper.getDestination(entityObject)
            return  op.copy().apply { companyId = entityObject.company.id }
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

         fun toEntity(businessObject: Opening, company: CompanyEntity): OpeningEntity {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(businessObject)
            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
            val openingBusinessModelToEntityMapper: JMapper<OpeningEntity, Opening> =
                JMapper(OpeningEntity::class.java, Opening::class.java)
            val s =  openingBusinessModelToEntityMapper.getDestination(businessObject)
             return s.copy(company = company)
        }
    }

    override fun toString(): String {
        return "Opening(" +
                "id=$id," +
                "keyWords=$keyWords," +
                "customKeyWords=$customKeyWords," +
                "hasTechnicalInterview=$hasTechnicalInterview," +
                "hasTechnicalTest=$hasTechnicalTest," +
                "periodCount=$periodCount," +
                "periodType=$periodType," +
                "openPositions=$openPositions," +
                "acceptOnClosingOpportunity=$acceptOnClosingOpportunity," +
                "signAgreement=$signAgreement," +
                "trainers=$trainers," +
                "available=$available," +
                "title=$title," +
                "description=$description," +
                "requirements=$requirements," +
                "restrictions=$restrictions," +
                "recruitmentProcess=$recruitmentProcess," +
                "startDate=$startDate)"
    }

    @Suppress("unused")
    constructor() : this(UUID.randomUUID())
}
