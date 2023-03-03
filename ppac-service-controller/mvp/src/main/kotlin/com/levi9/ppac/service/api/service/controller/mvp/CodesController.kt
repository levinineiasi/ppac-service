package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.CompanyCode
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
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CodesController(
    val companyCodeService: CodeService<CompanyCode>?,
    val log: Logger = Logger.getLogger(CodesController::class.java.name)
) {

    @GetMapping("/checkAdminCode")
    fun checkAdminCode(@RequestHeader("AdminCode") adminCode: Int): ResponseEntity<Any> {
        val isAdminCode = companyCodeService?.checkAdminCode(adminCode)
        if (isAdminCode!!) {
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("")
    fun findAll(@RequestHeader("AdminCode") adminCode: Int): ResponseEntity<Any> {

        log.info("Returning all company codes from database.")

        return companyCodeService?.let {
            ResponseEntity(it.findAll(adminCode), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{displayName}")
    fun createCode(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable displayName: String
    ): ResponseEntity<Any> {

        log.info("Create company code for $displayName company.")

        return companyCodeService?.let {
            val responseDto = it.createCompanyCode(adminCode, displayName)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{codeId}")
    fun deleteById(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable codeId: UUID
    ): ResponseEntity<Any> {

        log.info("Delete company code with code_id $codeId.")

        return companyCodeService?.let {
            it.deleteById(adminCode, codeId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)

    }
}