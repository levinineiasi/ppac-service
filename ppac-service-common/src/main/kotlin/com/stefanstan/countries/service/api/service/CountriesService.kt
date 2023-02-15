package com.levi9.ppac.service.api.service

interface CountriesService {
    fun getRoute(origin: String, destination: String): List<String>
}
