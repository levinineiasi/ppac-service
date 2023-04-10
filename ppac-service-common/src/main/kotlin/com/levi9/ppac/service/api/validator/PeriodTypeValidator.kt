package com.levi9.ppac.service.api.validator

import com.levi9.ppac.service.api.enums.PeriodType
import com.levi9.ppac.service.api.validator.annotations.ValidPeriodType
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PeriodTypeValidator : ConstraintValidator<ValidPeriodType, PeriodType> {
    override fun isValid(value: PeriodType?, context: ConstraintValidatorContext?): Boolean {
        if (value in PeriodType.values()) {
            return true
        }
        return false
    }
}
