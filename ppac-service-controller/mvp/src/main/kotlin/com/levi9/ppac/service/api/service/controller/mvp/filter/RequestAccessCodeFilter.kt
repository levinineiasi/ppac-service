package com.levi9.ppac.service.api.service.controller.mvp.filter

import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.security.SecurityContext
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


@Component
@Order
class RequestAccessCodeFilter(
    private val securityContext: SecurityContext<Int>
) : Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req = request as HttpServletRequest

        try {
            val accessCodeAsString = req.getHeader("AccessCode")
            if (accessCodeAsString.isNullOrEmpty()) {
                logger.debug("AccessCode header not set")
            } else {
                val accessCodeAsInt = accessCodeAsString.toInt()
                securityContext.setAccessCode(accessCodeAsInt)
            }
        } catch (e: NumberFormatException) {
            logger.debug("AccessCode with value ${req.getHeader("AccessCode")} is not a number")
        }
        chain?.doFilter(request, response)
    }
}
