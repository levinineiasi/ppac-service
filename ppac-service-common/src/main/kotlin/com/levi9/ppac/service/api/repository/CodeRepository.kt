package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

interface CodeRepository : JpaRepository<AccessCodeEntity, UUID> {

    @Query("""SELECT COUNT(*) = 1
            FROM AccessCodeEntity codes
            WHERE codes.value = :value
        AND codes.type = 'ADMIN_CODE'""")
    fun isAdminCode(@Param("value") value: Int): Boolean

    @Query("""SELECT COUNT(*) = 1
    FROM CompanyCodeEntity companyCode
    WHERE companyCode.accessCode.value = :value
    AND companyCode.accessCode.type = 'COMPANY_CODE'
    AND companyCode.company.id = :companyId"""
    )
    fun isCompanyCode(@Param("value") value: Int, @Param("companyId") companyId: UUID): Boolean

    @Query("""SELECT COUNT(*) = 1
            FROM AccessCodeEntity codes
            WHERE codes.value = :value""")
    fun isCodeIdPresent(@Param("value") value: Int): Boolean
}
