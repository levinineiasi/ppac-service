package com.levi9.ppac.service.api.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TRAINERS")
class TrainerEntity(

        @Id
        @Column(name = "ID")
        var id: UUID,

        @Column(name = "NAME", nullable = false)
        var name: String,

        @Column(name = "DESCRIPTION", nullable = false)
        var description: String,

        @Column(name = "LINKEDIN_URL", nullable = true)
        var linkedinURL: String,

        ) {

    @Column(name = "AVATAR", nullable = true)
    var avatar: String? = null
}