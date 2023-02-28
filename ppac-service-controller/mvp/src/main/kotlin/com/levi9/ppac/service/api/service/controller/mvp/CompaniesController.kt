package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.service.AuthorizationService
import com.levi9.ppac.service.api.service.CompanyService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/companies")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CompaniesController(
    val companyService: CompanyService<Company>?,
    val authorizationService: AuthorizationService
) {
    @GetMapping("")
    fun findAll(@RequestHeader("AdminCode") adminCode: Int): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyService?.let {
                ResponseEntity(it.findAll(), HttpStatus.OK)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("")
    fun createCompany(
        @RequestHeader("AdminCode") adminCode: Int,
        @RequestBody dto: Company
    ): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyService?.let {
                val responseDto = it.create(dto)
                ResponseEntity(responseDto, HttpStatus.CREATED)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{companyId}")
    fun deleteById(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyService?.let {
                it.deleteById(companyId)
                ResponseEntity(HttpStatus.NO_CONTENT)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}
