package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.CompanyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/v1/companies")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
@Tag(name = "Companies Controller")
class CompaniesController(
    private val companyService: CompanyService<Company>?
) {

    @Operation(
        summary = "Retrieves all companies",
        description = "Returns all companies"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = Company::class))
                )]
            ),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @GetMapping("")
    fun findAll(@RequestHeader("AccessCode") accessCode: Int): ResponseEntity<Any> {

        logger.info("Returning all companies from database.")

        return companyService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
            summary = "Retrieves company by id",
            description = "Returns a company by id"
    )
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        content = [Content(
                                mediaType = "application/json",
                                array = ArraySchema(schema = Schema(implementation = Company::class))
                        )]
                ),
                ApiResponse(responseCode = "404", description = "Not Found")
            ]
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID, @RequestParam(required = false, defaultValue = "true") onlyAvailableOpenings: Boolean): ResponseEntity<Any> {

        logger.info("Returning company by id from database.")

        return companyService?.let {
            ResponseEntity(it.findById(id, onlyAvailableOpenings), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
            summary = "Update a company",
            description = "Update a company"
    )
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        content = [Content(
                                mediaType = "application/json",
                                array = ArraySchema(schema = Schema(implementation = Company::class))
                        )]
                ),
                ApiResponse(responseCode = "401", description = "Unauthorized"),
                ApiResponse(responseCode = "404", description = "Not Found")
            ]
    )
    @PutMapping("/{id}")
    fun updateById(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable id: UUID,
        @RequestBody updatedCompany: Company
    ): ResponseEntity<Any> {

        logger.info("Update company with id $id.")

        return companyService?.let {
            val responseDto = it.updateById(id, updatedCompany)
            ResponseEntity(responseDto, HttpStatus.OK)
        }!!
    }


    @Operation(
        summary = "Add an opening to a company",
        description = "Add an opening to company specified by its id"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Opening::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @PostMapping("{companyId}/openings")
    fun addOpening(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable companyId: UUID,
        @RequestBody @Valid opening: Opening
    ): ResponseEntity<Any> {

        logger.info("Create an opening for company with id $companyId.")

        return companyService?.let {
            val responseDto = it.addOpening(companyId, opening)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }


    @Operation(
        summary = "Deletes a company",
        description = "Deletes a company by it's ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204"
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found")
        ]
    )
    @DeleteMapping("/{companyId}")
    fun deleteById(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {

        logger.info("Delete company with id $companyId .")

        return companyService?.let {
            it.deleteById(companyId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
