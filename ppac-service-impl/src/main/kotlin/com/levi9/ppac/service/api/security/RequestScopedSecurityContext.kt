package com.levi9.ppac.service.api.security

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class RequestScopedSecurityContext(
    private var accessCode: String = ""
): SecurityContext<Int> {
    override fun accessCodeIsSet(): Boolean =
            accessCode.isNotBlank() && accessCode.isNotEmpty() && accessCode.all { char -> char.isDigit() }

    override fun getAccessCode(): Int = if (accessCodeIsSet()) accessCode.toInt() else 0

    override fun setAccessCode(accessCode: Int) {
        this.accessCode = accessCode.toString()
    }
}
