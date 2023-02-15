package com.levi9.ppac.service.api.gateway.countries

import com.levi9.ppac.service.api.integration.v1.dto.CountryDto


interface CountriesGateway {
    fun getCountries(): Map<String, CountryDto>
}
