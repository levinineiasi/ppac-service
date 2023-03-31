package com.levi9.ppac.service.api.business.converter

interface Converter<B, E> {

    fun toBusinessModel(entityObject: E): B

    fun toEntity(businessObject: B): E
}