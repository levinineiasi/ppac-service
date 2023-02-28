package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.service.AuthorizationService
import com.levi9.ppac.service.api.service.CodeService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CodesController(
    val companyCodeService: CodeService<CompanyCode>?,
    val authorizationService: AuthorizationService
) {

    @GetMapping("")
    fun findAll(@RequestHeader("AdminCode") adminCode: Int): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyCodeService?.let {
                ResponseEntity(it.findAll(), HttpStatus.OK)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)

    }

    @PostMapping("/{displayName}")
    fun createCode(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable displayName: String
    ): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyCodeService?.let {
                val responseDto = it.createCompanyCode(displayName)
                ResponseEntity(responseDto, HttpStatus.CREATED)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{codeId}")
    fun deleteById(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable codeId: UUID
    ): ResponseEntity<Any> {
        if (authorizationService.isAdmin(adminCode)) {
            return companyCodeService?.let {
                it.deleteById(codeId)
                ResponseEntity(HttpStatus.NO_CONTENT)
            } ?: ResponseEntity("Feature flag for CompanyService not enabled", HttpStatus.NOT_IMPLEMENTED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}