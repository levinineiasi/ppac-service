package com.levi9.ppac.service.api.validator

import com.levi9.ppac.service.api.enums.CodeType
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CodeTypeValidator : ConstraintValidator<ValidCodeType, CodeType> {

    override fun isValid(value: CodeType?, context: ConstraintValidatorContext?): Boolean {
        if (value in CodeType.values()) {
            return true
        }
        return false
    }
}