package com.levi9.ppac.service.api.service

import com.levi9.ppac.service.api.data_classes.Student
import com.levi9.ppac.service.api.repository.CompanyRepository
import com.levi9.ppac.service.api.repository.StudentRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@ConditionalOnProperty(prefix = "feature", name = ["mvp"], havingValue = "true")
class StudentServiceImpl(
    private val studentRepository: StudentRepository,
    private val companyRepository: CompanyRepository
): StudentService<Student> {
    @Transactional
    override fun findAll(): List<Student> {
        return studentRepository.findAll().map { Student.parse(it) }
    }

    @Transactional
    override fun create(dto: Student): Student {
        val persistedStudent = studentRepository.save(
            Student.parse(dto).apply { id = UUID.randomUUID() }
        )
        return Student.parse(persistedStudent)
    }

    @Transactional
    override fun assignToCompany(studentId: UUID, companyId: UUID): Boolean {
        return companyRepository.findByIdOrNull(companyId)?.let { company ->
            studentRepository.findByIdOrNull(studentId)?.let { student ->
                val newStudentsSet = company.students?.toMutableSet()
                newStudentsSet?.add(student)
                company.students = newStudentsSet
                student.companyId = companyId
                true
            } ?: false
        } ?: false
    }

    @Transactional
    override fun deleteById(id: UUID) {
        studentRepository.deleteById(id)
    }
}
