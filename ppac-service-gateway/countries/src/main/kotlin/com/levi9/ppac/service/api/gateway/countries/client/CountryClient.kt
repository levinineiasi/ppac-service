package com.levi9.ppac.service.api.gateway.countries.client

import com.levi9.ppac.service.api.gateway.countries.dto.CountryResponseDto

interface CountryClient {
    fun getCountries(): List<CountryResponseDto>
}
