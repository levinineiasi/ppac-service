package com.levi9.ppac.service.api.service.controller.mvp

import com.googlecode.jmapper.JMapper
import com.levi9.ppac.service.api.data_classes.Opening
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
import java.util.UUID
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/openings")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
@Tag(name = "Openings Controller")
class OpeningsController(
    private val openingService: OpeningService<Opening>?
) {
    val openingBusinessToDtoMapper: JMapper<OpeningDto, Opening> = JMapper(OpeningDto::class.java, Opening::class.java)
    val openingDtoToBusinessMapper: JMapper<Opening, OpeningDto> = JMapper(Opening::class.java, OpeningDto::class.java)

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
                ),
                ApiResponse(responseCode = "404", description = "Not Found")
            ]
    )
    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {

        logger.info("Returning all companies from database.")

        return openingService?.let {
            ResponseEntity(it.findAll().map { opening ->
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
                ),
                ApiResponse(responseCode = "401", description = "Unauthorized"),
                ApiResponse(responseCode = "404", description = "Not Found")
            ]
    )
    @PutMapping("/{openingId}")
    fun updateById(
            @RequestHeader("AccessCode") accessCode: Int,
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
                ),
                ApiResponse(responseCode = "401", description = "Unauthorized"),
                ApiResponse(responseCode = "404", description = "Not Found")
            ]
    )
    @PatchMapping("/{openingId}/{available}")
    fun updateById(
            @RequestHeader("AccessCode") accessCode: Int,
            @PathVariable openingId: UUID,
            @PathVariable available: Boolean
    ): ResponseEntity<Any> {

        return openingService?.let {
            val responseDto = it.changeAvailability(openingId, available)
            ResponseEntity(openingBusinessToDtoMapper.getDestination(responseDto), HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
