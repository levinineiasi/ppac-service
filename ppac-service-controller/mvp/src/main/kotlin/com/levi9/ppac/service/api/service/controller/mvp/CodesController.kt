package com.levi9.ppac.service.api.service.controller.mvp

import com.googlecode.jmapper.JMapper
import com.levi9.ppac.service.api.business.CompanyCode
import com.levi9.ppac.service.api.integration.mvp.CompanyCodeDto
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.security.SecurityContext
import com.levi9.ppac.service.api.service.CodeService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
@OpenAPIDefinition(info = Info(title = "PPAC API - codes endpoint"))
@Tag(name = "Codes Controller")
class CodesController(
    private val securityContext: SecurityContext<Int>,
    private val codeService: CodeService<CompanyCode, UUID>?
) {
    val codesBusinessToDtoMapper: JMapper<CompanyCodeDto, CompanyCode> =
        JMapper(CompanyCodeDto::class.java, CompanyCode::class.java)

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
        summary = "Check if user has company rights",
        description = "Returns UNAUTHORIZED if the code received and companyId doesn't match"
    )
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "OK"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
        )]
    )
    @GetMapping("/checkCompanyCode/{companyId}")
    fun checkCompanyCode(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {
        if (securityContext.accessCodeIsSet()) {
            val isCompanyCode = codeService?.isCompanyCode(securityContext.getAccessCode(), companyId)
            if (isCompanyCode!!) {
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
                    array = ArraySchema(schema = Schema(implementation = CompanyCodeDto::class))
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "204", description = "No Content")
        ]
    )
    @GetMapping("")
    fun findAll(@RequestHeader("AccessCode") accessCode: Int): ResponseEntity<Any> {

        logger.info("Returning all company codes from database.")

        val listCompanies = codeService!!.findAll()
        if (listCompanies.isEmpty()) {
            return ResponseEntity("The requested resource has no content.", HttpStatus.NO_CONTENT)
        }

        return ResponseEntity(listCompanies.map {
            codesBusinessToDtoMapper.getDestination(it)
        }, HttpStatus.OK)
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
                        implementation = CompanyCodeDto::class
                    )
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
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
            ResponseEntity(codesBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
        }!!
    }
}
