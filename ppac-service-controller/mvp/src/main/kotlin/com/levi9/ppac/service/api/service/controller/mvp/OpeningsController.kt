package com.levi9.ppac.service.api.service.controller.mvp

import com.googlecode.jmapper.JMapper
import com.levi9.ppac.service.api.business.Opening
import com.levi9.ppac.service.api.integration.mvp.OpeningDto
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.OpeningService
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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RestController
@RequestMapping("/api/v1/openings")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Tag(name = "Openings Controller")
@Validated
class OpeningsController(
    private val openingService: OpeningService<Opening, UUID>?
) {
    val openingBusinessToDtoMapper: JMapper<OpeningDto, Opening> = JMapper(OpeningDto::class.java, Opening::class.java)
    val openingDtoToBusinessMapper: JMapper<Opening, OpeningDto> = JMapper(Opening::class.java, OpeningDto::class.java)

    @Operation(
        summary = "Retrieves opening by id",
        description = "Returns a opening by id"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = OpeningDto::class))
                )]
            )
        ]
    )
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID,
        @RequestParam(required = false, defaultValue = "true") onlyAvailableOpenings: Boolean
    ): ResponseEntity<Any> {

        logger.info("Returning opening by id from database.")

        return openingService?.let {
            ResponseEntity(
                openingBusinessToDtoMapper.getDestination(it.findById(id)),
                HttpStatus.OK
            )
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
        summary = "Retrieves all openings",
        description = "Returns all openings"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = OpeningDto::class))
                )]
            )
        ]
    )
    @GetMapping("")
    fun findAll(@RequestParam(required = false) count: Int?): ResponseEntity<Any> {

        logger.info("Returning all companies from database.")

        return openingService?.let {
            val openings =
                    if (count != null) {
                it.findAllFirstCount(count)
            } else {
                it.findAll()
            }

            ResponseEntity(openings.map { opening ->
                openingBusinessToDtoMapper.getDestination(opening)
            }, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
        summary = "Update an opening",
        description = "Update an opening"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = OpeningDto::class))
                )]
            )
        ]
    )
    @PutMapping("/{openingId}")
    fun updateById(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable openingId: UUID,
        @RequestBody @Valid opening: OpeningDto
    ): ResponseEntity<Any> {

        return openingService?.let {
            val responseDto = it.updateOpening(openingId, openingDtoToBusinessMapper.getDestination(opening))
            ResponseEntity(openingBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(
        summary = "Update opening's availability",
        description = "Update opening's availability"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = OpeningDto::class))
                )]
            )
        ]
    )
    @PatchMapping("/{openingId}/{available}")
    fun updateOpeningAvailabilityById(
        @RequestHeader("AccessCode")
        @Min(value = 100000, message = "Invalid length for header AccessCode.")
        @Max(value = 999999, message = "Invalid length for header AccessCode.")
        accessCode: Int,
        @PathVariable openingId: UUID,
        @PathVariable available: Boolean
    ): ResponseEntity<Any> {

        return openingService?.let {
            val responseDto = it.changeAvailability(openingId, available)
            ResponseEntity(openingBusinessToDtoMapper.getDestination(responseDto), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
