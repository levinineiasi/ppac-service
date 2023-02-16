package com.levi9.ppac.service.api.gateway.countries.client

import com.levi9.ppac.service.api.gateway.countries.config.CountryClientFeignConfig
import com.levi9.ppac.service.api.gateway.countries.dto.CountryResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

// I could have simply downloaded the json file and loaded from resources, but decided to get it instead from URL
@Profile("wiremocked")
@FeignClient(value = "countries", url = "http://localhost:9000/", configuration = [CountryClientFeignConfig::class])
interface LocalCountryClient: CountryClient {
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/mledoze/countries/master/countries.json"]
    )
    override fun getCountries(): List<CountryResponseDto>
}
