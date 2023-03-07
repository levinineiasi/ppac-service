package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.security.SecurityContext
import com.levi9.ppac.service.api.service.CodeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class CodesController(
    private val securityContext: SecurityContext<Int>,
    private val codeService: CodeService<CompanyCode>?,
) {
    @Operation(
        summary = "Check if user has admin rights",
        description = "Returns UNAUTHORIZED if the code received is not for admin access"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping("/checkAdminCode")
    fun checkAdminCode(@RequestHeader("AccessCode") accessCode: Int): ResponseEntity<Any> {
        if (securityContext.accessCodeIsSet()) {
            val isAdminCode = codeService?.isAdminCode(securityContext.getAccessCode())
            if (isAdminCode!!) {
                return ResponseEntity(HttpStatus.OK)
            }
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @Operation(
        summary = "Retrieves all codes",
        description = "Returns all company codes"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = CompanyCode::class))
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @GetMapping("")
    fun findAll(@RequestHeader("AccessCode") accessCode: Int): ResponseEntity<Any> {

        logger.info("Returning all company codes from database.")

        return codeService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
        summary = "Create access code for company",
        description = "Create a company and generate it's access code"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = CompanyCode::class
                    )
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @PostMapping("/{displayName}")
    fun createCode(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable displayName: String
    ): ResponseEntity<Any> {

        logger.info("Create company code for $displayName company.")

        return codeService?.let {
            val responseDto = it.createCompanyCode(displayName)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(summary = "Deletes access code for a company")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "No content"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @DeleteMapping("/{codeId}")
    fun deleteById(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable codeId: UUID
    ): ResponseEntity<Any> {

        logger.info("Delete company code with code_id $codeId.")

        return codeService?.let {
            it.deleteById(codeId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
