package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.UUID

data class CompanyCode(
    val id: UUID,
    val accessCode: AccessCode,
    val company: Company
) {

    companion object ConverterImpl : Converter<CompanyCode, CompanyCodeEntity> {
        override fun toBusinessModel(elemEntity: CompanyCodeEntity): CompanyCode {
            return CompanyCode(
                elemEntity.id,
                AccessCode.toBusinessModel(elemEntity.accessCode),
                Company.toBusinessModel(elemEntity.company)
            )
        }

        override fun toEntity(businessObject: CompanyCode): CompanyCodeEntity {
            return CompanyCodeEntity(
                businessObject.id,
                AccessCode.toEntity(businessObject.accessCode),
                Company.toEntity(businessObject.company)
            )
        }
    }

    override fun toString(): String {
        return "CompanyCode(id=$id, accessCode=$accessCode, company=$company)"
    }
}
