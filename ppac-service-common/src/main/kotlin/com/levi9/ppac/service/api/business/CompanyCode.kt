package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.business.converter.Converter
import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import java.util.UUID
import javax.persistence.Id

data class CompanyCode(
    @field:Id
    val id: UUID,
    val accessCode: AccessCode,
    val company: Company
) {

    companion object ConverterImpl : Converter<CompanyCode, CompanyCodeEntity> {
        override fun toBusinessModel(entityObject: CompanyCodeEntity): CompanyCode {
            return CompanyCode(
                entityObject.id,
                AccessCode.toBusinessModel(entityObject.accessCode),
                Company.toBusinessModel(entityObject.company)
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
        return "CompanyCode(" +
                "id=$id," +
                "accessCode = $accessCode," +
                "company = $company)"
    }
}
