package com.levi9.ppac.service.api.algorithm

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.levi9.ppac.service.api.algorithm.dto.CountryResponseDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class ShortestPathTest {
    companion object {
        val objectMapper: ObjectMapper = retrieveObjectMapper()

        private fun retrieveObjectMapper(): ObjectMapper {
            val simpleModule = SimpleModule()
            simpleModule.addSerializer(OffsetDateTime::class.java, object : JsonSerializer<OffsetDateTime?>() {
                @Throws(IOException::class, JsonProcessingException::class)
                override fun serialize(
                    offsetDateTime: OffsetDateTime?,
                    jsonGenerator: JsonGenerator,
                    serializerProvider: SerializerProvider?
                ) {
                    jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime))
                }
            })
            return jacksonObjectMapper()
                .findAndRegisterModules()
                .registerKotlinModule().registerModule(JavaTimeModule())
                .registerModule(simpleModule)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }

    @Test
    fun `should find shortest path between two countries using provided data`() {
        val countriesDataAsString = ShortestPathTest::class.java.getResource("/countries.json")?.readText()
        val countryResponseDtoList =
            objectMapper.readValue(countriesDataAsString, object : TypeReference<List<CountryResponseDto>>() {})

        val graph = countryResponseDtoList.flatMap {
                countryResponseDto -> countryResponseDto.borders.map { Edge(countryResponseDto.symbol, it, 1) }
        }

        var result = findShortestPath(graph, "CZE", "ITA")
        Assertions.assertEquals(listOf("CZE", "AUT", "ITA"), result.shortestPath())

        result = findShortestPath(graph, "ESP", "POL")
        Assertions.assertEquals(listOf("ESP", "FRA", "DEU", "POL"), result.shortestPath())

        result = findShortestPath(graph, "ROU", "ENG")
        Assertions.assertEquals(emptyList<String>(), result.shortestPath())

        result = findShortestPath(graph, "BRA", "USA")
        Assertions.assertEquals(
            listOf("BRA", "COL", "PAN", "CRI", "NIC", "HND", "GTM", "MEX", "USA"),
            result.shortestPath()
        )

        result = findShortestPath(graph, "VNM", "ROU")
        Assertions.assertEquals(listOf("VNM", "CHN", "RUS", "UKR", "ROU"), result.shortestPath())
    }

    @Test
    fun `should find shortest path`() {
        val graph = listOf(
            Edge("a", "b", 4),
            Edge("a", "c", 2),
            Edge("b", "c", 3),
            Edge("c", "b", 1),
            Edge("c", "d", 5),
            Edge("b", "d", 1),
            Edge("a", "e", 1),
            Edge("e", "d", 4)
        )
        val result = findShortestPath(graph, "a", "d")

        Assertions.assertEquals(listOf("a", "c", "b", "d"), result.shortestPath())
        Assertions.assertEquals(4, result.shortestDistance())
    }

    @Test
    fun `should behave when shortest path is not reachable`() {
        val graph = listOf(
            Edge("a", "b", 4),
            Edge("a", "c", 2),
            Edge("b", "c", 3),
            Edge("c", "b", 1),
            Edge("c", "d", 5),
            Edge("b", "d", 1),
            Edge("e", "d", 4)
        )
        val result = findShortestPath(graph, "a", "e")

        Assertions.assertEquals(emptyList<String>(), result.shortestPath())
        Assertions.assertEquals(null, result.shortestDistance())
    }

    @Test
    fun `should behave when to-node doesnt event exist`() {
        val graph = listOf(
            Edge("a", "b", 4),
            Edge("a", "c", 2),
            Edge("b", "c", 3),
            Edge("c", "b", 1),
            Edge("c", "d", 5),
            Edge("b", "d", 1),
            Edge("a", "e", 1),
            Edge("e", "d", 4)
        )
        val result = findShortestPath(graph, "a", "f")

        Assertions.assertEquals(emptyList<String>(), result.shortestPath())
        Assertions.assertEquals(null, result.shortestDistance())
    }

    @Test
    fun `should behave when the world is empty`() {
        val graph = emptyList<Edge<String>>()
        val result = findShortestPath(graph, "a", "f")

        Assertions.assertEquals(emptyList<String>(), result.shortestPath())
        Assertions.assertEquals(null, result.shortestDistance())
    }
}
