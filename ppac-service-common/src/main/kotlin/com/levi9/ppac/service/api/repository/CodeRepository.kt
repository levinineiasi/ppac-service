package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CodeRepository : JpaRepository<AccessCodeEntity, UUID> {

    fun findByValue(value: Int): AccessCodeEntity?

}