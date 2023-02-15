package com.levi9.ppac.service.api.algorithm.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal class CountryResponseDto (
    @JsonProperty("name")
    val name: CountryName,
    @JsonProperty("cca3")
    val symbol: String,
    @JsonProperty("borders")
    val borders: List<String>
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal class CountryName (
    @JsonProperty("official")
    val official: String
)
