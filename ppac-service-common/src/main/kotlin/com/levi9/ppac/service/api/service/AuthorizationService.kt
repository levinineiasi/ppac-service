package com.levi9.ppac.service.api.service

interface AuthorizationService {

    fun isAdmin(code: Int): Boolean
}