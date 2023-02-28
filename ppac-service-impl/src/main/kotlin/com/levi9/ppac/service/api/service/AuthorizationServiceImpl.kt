package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.enums.CodeType
import com.levi9.ppac.service.api.repository.CodeRepository
import org.springframework.stereotype.Service

@Service
class AuthorizationServiceImpl(
    private val codeRepository: CodeRepository
) : AuthorizationService {

    override fun isAdmin(code: Int): Boolean {

        val codeType = codeRepository.findByValue(code)?.type

        if (codeType?.equals(CodeType.ADMIN_CODE) == true) {
            return true
        }
        return false
    }
}