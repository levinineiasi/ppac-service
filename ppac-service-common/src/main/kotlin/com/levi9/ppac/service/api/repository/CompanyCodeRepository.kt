package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.CompanyCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyCodeRepository : JpaRepository<CompanyCodeEntity,UUID> {
    @Query("""SELECT COUNT(*) = 1 FROM CompanyCodeEntity t 
            WHERE t.accessCode.value = :value 
            AND t.accessCode.type = 'COMPANY_CODE'
            AND t.company.id = :companyId""")
    fun isCompanyCode( @Param("companyId") companyId: UUID, @Param("value") value: Int): Boolean
}
