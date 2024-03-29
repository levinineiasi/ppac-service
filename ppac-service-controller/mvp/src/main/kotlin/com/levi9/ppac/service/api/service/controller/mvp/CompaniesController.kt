package com.levi9.ppac.service.api.service.controller.mvp

import com.googlecode.jmapper.JMapper
import com.levi9.ppac.service.api.business.Company
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.integration.mvp.CompanyDto
import com.levi9.ppac.service.api.integration.mvp.OpeningDto
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.CompanyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/companies")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Tag(name = "Companies Controller")
@Validated
class CompaniesController(
    private val companyService: CompanyService<Company, UUID, Opening>?
) {
    val companyBusinessToDtoMapper: JMapper<CompanyDto, Company> = JMapper(CompanyDto::class.java, Company::class.java)
    val companyDtoToBusinessMapper: JMapper<Company, CompanyDto> = JMapper(Company::class.java, CompanyDto::class.java)

    val openingBusinessToDtoMapper: JMapper<OpeningDto, Opening> = JMapper(OpeningDto::class.java, Opening::class.java)
    val openingDtoToBusinessMapper: JMapper<Opening, OpeningDto> = JMapper(Opening::class.java, OpeningDto::class.java)

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
                    array = ArraySchema(schema = Schema(implementation = CompanyDto::class))
                )]
            )
        ]
    )
    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {

        logger.info("Returning all companies from database.")

        return companyService?.let {
            ResponseEntity(it.findAll().map { company ->
                companyBusinessToDtoMapper.getDestination(company)
            }, HttpStatus.OK)
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
                    array = ArraySchema(schema = Schema(implementation = CompanyDto::class))
                )]
            )
        ]
    )
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID,
        @RequestParam(required = false, defaultValue = "true") onlyAvailableOpenings: Boolean
    ): ResponseEntity<Any> {

        logger.info("Returning company by id from database.")

        return companyService?.let {
            ResponseEntity(
                companyBusinessToDtoMapper.getDestination(it.findById(id, onlyAvailableOpenings)),
                HttpStatus.OK
            )
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
        summary = "Update a company",
        description = "Update a company"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = CompanyDto::class))
                )]
            )
        ]
    )
    @PutMapping("/{id}")
    fun updateById(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable id: UUID,
        @RequestBody updatedCompany: CompanyDto
    ): ResponseEntity<Any> {

        logger.info("Update company with id $id.")

        return companyService?.let {
            val responseDto = it.updateById(id, companyDtoToBusinessMapper.getDestination(updatedCompany))
            ResponseEntity(companyBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
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
                    schema = Schema(implementation = OpeningDto::class)
                )]
            )
        ]
    )
    @PostMapping("{companyId}/openings")
    fun addOpening(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable companyId: UUID,
        @RequestBody opening: OpeningDto
    ): ResponseEntity<Any> {

        logger.info("Create an opening for company with id $companyId.")

        return companyService?.let {
            val responseDto = it.addOpening(companyId, openingDtoToBusinessMapper.getDestination(opening))
            ResponseEntity(openingBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
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
            )
        ]
    )
    @DeleteMapping("/{companyId}")
    fun deleteById(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {

        logger.info("Delete company with id $companyId .")

        return companyService?.let {
            it.deleteById(companyId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
