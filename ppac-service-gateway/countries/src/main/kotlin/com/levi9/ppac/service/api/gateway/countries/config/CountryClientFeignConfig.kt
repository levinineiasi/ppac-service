package com.levi9.ppac.service.api.gateway.countries.config

import feign.codec.Decoder
import feign.jackson.JacksonDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class CountryClientFeignConfig {
    @Bean
    fun feignDecoder(): Decoder? {
        return JacksonDecoder()
    }
}
