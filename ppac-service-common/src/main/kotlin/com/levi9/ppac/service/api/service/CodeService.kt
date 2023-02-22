package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.AccessCode

interface CodeService<T> : CrudService<T>{

    fun create(dto: AccessCode, displayName:String): AccessCode
}