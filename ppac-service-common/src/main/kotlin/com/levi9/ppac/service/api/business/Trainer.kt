package com.levi9.ppac.service.api.business

import com.levi9.ppac.service.api.domain.TrainerEntity
import java.util.*

class Trainer {
    var id: UUID = UUID.randomUUID()
    var name: String = ""
    var description: String = ""
    var linkedinURL: String? = null
    var avatar: String? = null

    companion object {
        fun parse(elem: TrainerEntity): Trainer {
            return Trainer().apply {
                id = elem.id
                name = elem.name
                description = elem.description
                linkedinURL = elem.linkedinURL
                avatar = elem.avatar
            }
        }

        fun parse(elem: Trainer): TrainerEntity {
            return TrainerEntity(
                    elem.id,
                    elem.name,
                    elem.description,
            ).apply {
                linkedinURL = elem.linkedinURL
                avatar = elem.avatar
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Trainer) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (linkedinURL != other.linkedinURL) return false
        if (avatar != other.avatar) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (linkedinURL?.hashCode() ?: 0)
        result = 31 * result + (avatar?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Trainer(id=$id, name='$name', description='$description', linkedinURL=$linkedinURL, avatar=$avatar)"
    }
}
