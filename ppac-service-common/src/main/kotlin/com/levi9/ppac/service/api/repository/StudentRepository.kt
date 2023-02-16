package com.levi9.ppac.service.api.repository

import com.levi9.ppac.service.api.domain.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository: JpaRepository<Student, UUID>
