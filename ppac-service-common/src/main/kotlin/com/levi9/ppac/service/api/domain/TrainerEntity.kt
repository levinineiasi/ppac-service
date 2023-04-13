package com.levi9.ppac.service.api.domain

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "TRAINERS")
data class TrainerEntity(

    @field:Id
    @Column(name = "ID", nullable = false)
    var id: UUID,

    @Column(name = "NAME", nullable = false)
    @field:Size(min = 3, max = 50, message = "Invalid length for name field.")
    var name: String,

    @Column(name = "ROLE", nullable = false)
    @field:Size(max = 150, message = "Invalid length for role field.")
    var role: String,

    ) {
    @Column(name = "LINKEDIN_URL", nullable = true)
    @field:Size(min = 15, max = 100, message = "Invalid size Linkedin URL")
    var linkedinURL: String? = null

    @Column(name = "AVATAR", nullable = true)
    var avatar: ByteArray? = null
}
