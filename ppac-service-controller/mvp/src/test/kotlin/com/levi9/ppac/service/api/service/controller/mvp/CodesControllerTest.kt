package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.AccessCode
import com.levi9.ppac.service.api.data_classes.Company
import com.levi9.ppac.service.api.data_classes.CompanyCode
import com.levi9.ppac.service.api.service.CodeService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(classes = [CodesController::class])
class CodesControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var codeService: CodeService<CompanyCode>

    private val idCompanyCode: UUID = UUID.randomUUID()
    private val idAccessCode: UUID = UUID.randomUUID()
    private val idCompany: UUID = UUID.randomUUID()
    private val companyCodeList : List<CompanyCode> = listOf(CompanyCode(idCompanyCode, AccessCode(idAccessCode, 100001), Company(idCompany, "Levi")))
    private val allCodesUrl = "http://localhost:8080/api/v1/codes"

    @Test
    fun `first test`() {
        every { codeService.findAll() } returns companyCodeList

        mockMvc.get(allCodesUrl) {
            header("AdminCode", "234567")
        }.andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("\$.[0].id") { value(idCompanyCode)}
                }
        verify(exactly = 1) { codeService.findAll() }

//        mockMvc.perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk)
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(idCompanyCode))

//        val requestBuilder = MockMvcRequestBuilders.get(allCodesUrl)
//                .header("AdminCode", "234567")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)

    }
}