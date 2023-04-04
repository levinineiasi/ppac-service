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
    @Column(name = "ID")
    var id: UUID,

    @Column(name = "NAME", nullable = false)
    @field:Size(min = 2, max = 30, message = "The name length should have between 2 and 30 characters.")
    var name: String,

    @Column(name = "DESCRIPTION", nullable = false)
    @field:Size(min = 40, max = 1000, message = "Invalid length for description field.")
    var description: String,

    ) {
    @Column(name = "LINKEDIN_URL", nullable = true)
    @field:Size(min = 20, max = 100, message = "Invalid size Linkedin URL")
    var linkedinURL: String? = null

    @Column(name = "AVATAR", nullable = true)
    var avatar: ByteArray? = null
}
