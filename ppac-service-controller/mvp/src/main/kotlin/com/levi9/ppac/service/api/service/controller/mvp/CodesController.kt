package com.levi9.ppac.service.api.service.controller.mvp

import com.googlecode.jmapper.JMapper
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.integration.mvp.CompanyDto
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.CodeService
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.OK
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
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/v1/codes")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@OpenAPIDefinition(info = Info(title = "PPAC API - Codes Endpoints"))
@Tag(name = "Codes Controller")
@Validated
class CodesController(
    private val codeService: CodeService<Company, UUID>?
) {
    val codesBusinessToDtoMapper: JMapper<CompanyDto, Company> =
        JMapper(CompanyDto::class.java, Company::class.java)

    @Operation(
        summary = "Check if user has admin rights",
        description = "Returns UNAUTHORIZED if the code received is not for admin access"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Nu-l gaseste")
        ]
    )
    @GetMapping("/checkAdminCode")
    fun checkAdminCode(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode")
        @Max(value = 999999, message = "Invalid length for header AccessCode")
        accessCode: Int
    ): ResponseEntity<Any> {
        codeService?.checkAdminCode()
        return ResponseEntity.status(HttpStatus.OK).body(OK)
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
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {
        codeService?.checkCompanyCode(companyId)
        return ResponseEntity.status(HttpStatus.OK).body(OK)
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
                    array = ArraySchema(schema = Schema(implementation = CompanyDto::class))
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "204", description = "No Content")
        ]
    )
    @GetMapping("")
    fun findAll(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int
    ): ResponseEntity<Any> {

        logger.info("Returning all company codes from database.")

        val listCompanies = codeService!!.findAll()
        if (listCompanies.isEmpty()) {
            return ResponseEntity.noContent().build()
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
                        implementation = CompanyDto::class
                    )
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @PostMapping("/{name}")
    fun createCode(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable
        @Size(min = 2, max =30 , message =  "Invalid length for name field")
        name: String
    ): ResponseEntity<Any> {

        logger.info("Create company code for $name company.")

        return codeService?.let {
            val responseDto = it.createCompanyCode(name)
            ResponseEntity(codesBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
        }!!
    }
}
