package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.algorithm.Edge
import com.levi9.ppac.service.api.algorithm.findShortestPath
import com.levi9.ppac.service.api.gateway.countries.CountriesGateway
import com.levi9.ppac.service.api.integration.v1.dto.CountryDto
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "feature", name = ["ppac-service"], havingValue = "true")
internal class CountriesServiceImpl(
    val `countriesGateway`: CountriesGateway
): CountriesService {
    override fun getRoute(origin: String, destination: String): List<String> {
        val countriesMap = countriesGateway.getCountries()

        if (!countriesMap.containsKey(origin) || !countriesMap.containsKey(destination)) {
            return emptyList()
        }

        val graph = prepareGraph(countriesMap)
        val result = findShortestPath(graph, origin, destination)
        return result.shortestPath()
    }

    private fun prepareGraph(countriesMap: Map<String, CountryDto>): List<Edge<String>> {
        return countriesMap.values.flatMap {
                countryDto -> countryDto.borders.map { Edge(countryDto.symbol, it, 1) }
        }
    }
}
