package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CodeRepository : JpaRepository<AccessCodeEntity, UUID> {

    @Query("SELECT COUNT(*) = 1 FROM AccessCodeEntity t WHERE t.value = :value AND t.type = 'ADMIN_CODE'")
    fun isAdminCode(@Param("value") value: Int): Boolean

    @Query("SELECT COUNT(*) = 1 FROM AccessCodeEntity t WHERE t.value = :value")
    fun isCodeIdPresent(@Param("value") value: Int): Boolean

}
