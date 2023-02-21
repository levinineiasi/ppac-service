package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Code

interface CodeService<T> : CrudService<T>{

    fun create(dto: Code, displayName:String): Code
}