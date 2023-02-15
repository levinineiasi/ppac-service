package com.levi9.ppac.service.api.gateway.countries.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class CountryResponseDto (
    @JsonProperty("name")
    val name: CountryName,
    @JsonProperty("cca3")
    val symbol: String,
    @JsonProperty("borders")
    val borders: List<String>
)

@JsonIgnoreProperties(ignoreUnknown = true)
class CountryName (
    @JsonProperty("official")
    val official: String
)
