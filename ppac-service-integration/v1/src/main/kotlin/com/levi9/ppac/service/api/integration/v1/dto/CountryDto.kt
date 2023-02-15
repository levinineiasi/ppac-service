package com.levi9.ppac.service.api.integration.v1.dto

class CountryDto(
    val name: String,
    val symbol: String,
    val borders: List<String> = emptyList()
)
