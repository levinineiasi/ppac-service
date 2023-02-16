package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.service.CompanyService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/companies")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CompaniesController(
    val companyService: CompanyService<Company>?
) {
    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {
        return companyService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("")
    fun createCompany(
        @RequestBody dto: Company
    ): ResponseEntity<Any> {
        return companyService?.let {
            val responseDto = it.create(dto)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{companyId}")
    fun deleteById(
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {
        return companyService?.let {
            it.deleteById(companyId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }
}
