package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.AccessCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CodeRepository : JpaRepository<AccessCodeEntity, UUID> {

    fun findByValue(value: Int): AccessCodeEntity?

}