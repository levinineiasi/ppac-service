package com.levi9.ppac.service.api.security

interface SecurityContext<T> {
    fun accessCodeIsSet(): Boolean
    fun getAccessCode(): T
    fun setAccessCode(accessCode: T)
}
