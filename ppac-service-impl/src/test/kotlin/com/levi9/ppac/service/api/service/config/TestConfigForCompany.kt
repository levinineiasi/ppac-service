package com.levi9.ppac.service.api.service.config

import com.levi9.ppac.service.api.security.RequestScopedSecurityContext
import com.levi9.ppac.service.api.security.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfigForCompany {
    @Bean
    fun getSecurityContext(): SecurityContext<Int> = RequestScopedSecurityContext("112233")
}
