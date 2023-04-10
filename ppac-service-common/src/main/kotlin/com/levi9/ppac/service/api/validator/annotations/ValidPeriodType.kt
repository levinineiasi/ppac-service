package com.levi9.ppac.service.api.validator.annotations

import com.levi9.ppac.service.api.validator.PeriodTypeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PeriodTypeValidator::class])

annotation class ValidPeriodType(
    val message: String = "Not allowed value for Code Type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
