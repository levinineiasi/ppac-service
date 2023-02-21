package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Code
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
    val codeService: CodeService<Code>?
) {

    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {
        return codeService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("")
    fun createCompany(
        @RequestParam displayName: String,
        @RequestBody dto: Code
    ): ResponseEntity<Any> {
        return codeService?.let {
            val responseDto = it.create(dto, displayName)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{codeId}")
    fun deleteById(
        @PathVariable codeId: UUID
    ): ResponseEntity<Any> {
        return codeService?.let {
            it.deleteById(codeId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }
}