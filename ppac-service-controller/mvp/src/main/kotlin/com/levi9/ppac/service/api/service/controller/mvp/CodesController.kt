package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.service.CodeService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CodesController(
    val companyCodeService: CodeService<CompanyCode>?
) {

    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {
        return companyCodeService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("/{displayName}")
    fun createCode(
        @PathVariable displayName: String
    ): ResponseEntity<Any> {
        return companyCodeService?.let {
            val responseDto = it.createCompanyCode(displayName)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{codeId}")
    fun deleteById(
        @PathVariable codeId: UUID
    ): ResponseEntity<Any> {
        return companyCodeService?.let {
            it.deleteById(codeId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }
}