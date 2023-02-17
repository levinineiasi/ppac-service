package com.levi9.ppac.service.api.service.controller.mvp

import com.levi9.ppac.service.api.data_classes.Student
import com.levi9.ppac.service.api.service.StudentService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/v1/students")
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
@Suppress("MagicNumber")
class StudentsController(
    val studentService: StudentService<Student>?
) {
    @GetMapping("")
    fun findAll(): ResponseEntity<Any> {
        return studentService?.let {
            ResponseEntity(it.findAll(), HttpStatus.OK)
        } ?: ResponseEntity("Feature flag for StudentsService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("")
    fun createStudent(
        @RequestBody dto: Student
    ): ResponseEntity<Any> {
        return studentService?.let {
            val responseDto = it.create(dto)
            ResponseEntity(responseDto, HttpStatus.CREATED)
        } ?: ResponseEntity("Feature flag for StudentsService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("/{studentId}/{companyId}")
    fun assignToCompany(
        @PathVariable studentId: UUID,
        @PathVariable companyId: UUID
    ): ResponseEntity<Any> {
        return studentService?.let {
            val response = it.assignToCompany(studentId, companyId)
            if (response) {
                ResponseEntity(HttpStatus.CREATED)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } ?: ResponseEntity("Feature flag for StudentsService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{studentId}")
    fun deleteById(
        @PathVariable studentId: UUID
    ): ResponseEntity<Any> {
        return studentService?.let {
            it.deleteById(studentId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity("Feature flag for StudentsService not enabled", HttpStatus.NOT_IMPLEMENTED)
    }
}
