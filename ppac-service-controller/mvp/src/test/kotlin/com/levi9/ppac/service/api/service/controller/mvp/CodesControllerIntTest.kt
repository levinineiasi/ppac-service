//package com.levi9.ppac.service.api.service.controller.mvp
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.web.client.TestRestTemplate
//import org.springframework.http.HttpEntity
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpMethod
//import org.springframework.http.HttpStatus
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.reactive.server.WebTestClient
//
//
//@SpringBootTest(
////    classes = arrayOf(ApiApplication::class),
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
//@ActiveProfiles("test")
//@AutoConfigureWebTestClient
//class CodesControllerIntTest {
//
//
////    @Autowired
////    lateinit var webTestClient: WebTestClient
////
////    @Test
////    fun postCode(){
////
////        webTestClient.post().he
////
////    }
//
//
//    @Autowired
//    lateinit var restTemplate: TestRestTemplate
//
//    @Test
//    fun whenCheckAdminCode_thenShouldOk() {
//
//        val headers = HttpHeaders()
//        headers["accessCode"] = "123456"
//        val entity = HttpEntity<String>(headers)
//        val response = restTemplate.exchange(
//            "/api/v1/codes/checkAdminCode", HttpMethod.GET, entity,
//            String::class.java
//        )
//
//        assertNotNull(response)
//        assertEquals(HttpStatus.OK, response?.statusCode)
//    }
//}
