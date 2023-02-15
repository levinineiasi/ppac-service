package com.levi9.ppac.service.api.service.controller.countries_v1

import com.levi9.ppac.service.api.gateway.countries.CountriesGateway
import com.levi9.ppac.service.api.integration.v1.dto.RouteResponseDto
import com.levi9.ppac.service.api.service.CountriesService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/routing")
@ConditionalOnProperty(prefix = "feature", name = ["ppac-v1"], havingValue = "true")
@Suppress("MagicNumber")
class CountriesController(
    val countriesService: CountriesService?,
    val countriesGateway: CountriesGateway
) {

    @GetMapping("/{origin}/{destination}")
    fun getRoute(
        @PathVariable origin: String,
        @PathVariable destination: String
    ): ResponseEntity<Any> {
        return  countriesService?.let { countriesService ->
            val allCountries = countriesGateway.getCountries()

            val originSymbol = origin.uppercase(Locale.getDefault())
            val destSymbol = destination.uppercase(Locale.getDefault())

            if (!allCountries.containsKey(originSymbol)) {
                return ResponseEntity("$origin is not a valid country symbol", HttpStatus.BAD_REQUEST)
            }
            if (!allCountries.containsKey(destSymbol)) {
                return ResponseEntity("$destination is not a valid country symbol", HttpStatus.BAD_REQUEST)
            }

            val route = countriesService.getRoute(originSymbol, destSymbol)

            if (route.isEmpty()) {
                return ResponseEntity("there is no land crossing from $origin to $destination", HttpStatus.BAD_REQUEST)
            }

            ResponseEntity(RouteResponseDto(route), HttpStatus.OK)
        } ?: ResponseEntity("Feature flag for CountriesService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }
}