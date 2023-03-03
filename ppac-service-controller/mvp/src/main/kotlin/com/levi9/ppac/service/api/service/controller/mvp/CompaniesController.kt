package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.service.CompanyService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.logging.Logger

@RestController
@RequestMapping("/v1/companies")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CompaniesController(
    val companyService: CompanyService<Company>?,
    val log: Logger = Logger.getLogger(CompaniesController::class.java.name)

) {
    @GetMapping("")
    fun findAll(@RequestHeader("AdminCode") adminCode: Int): ResponseEntity<Any> {

        log.info("Returning all companies from database.")

        return companyService?.let {
            ResponseEntity(it.findAll(adminCode), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{companyId}")
    fun deleteById(
        @RequestHeader("AdminCode") adminCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {

        log.info("Delete company with id $companyId .")

        return companyService?.let {
            it.deleteById(adminCode, companyId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
