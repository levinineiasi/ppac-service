package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Opening
import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.OpeningService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/openings")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class OpeningsController(
    private val openingService: OpeningService<Opening>?
) {

    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {

        logger.info("Returning all companies from database.")

        return openingService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{openingId}")
    fun updateById(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable openingId: UUID,
        @RequestBody opening: Opening
    ): ResponseEntity<Any> {

        return openingService?.let {
            val responseDto = it.updateOpening(openingId, opening)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }


    @PatchMapping("/{openingId}/{available}")
    fun updateById(
        @RequestHeader("AccessCode") accessCode: Int,
        @PathVariable openingId: UUID,
        @PathVariable available: Boolean
    ): ResponseEntity<Any> {

        return openingService?.let {
            val responseDto = it.availability(openingId, available)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}