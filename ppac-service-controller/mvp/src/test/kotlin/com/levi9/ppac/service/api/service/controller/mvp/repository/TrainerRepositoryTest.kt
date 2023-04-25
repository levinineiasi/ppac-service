package com.levi9.ppac.service.api.service.controller.mvp.repository

import com.levi9.ppac.service.api.repository.TrainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class TrainerRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var trainerRepository: TrainerRepository
}