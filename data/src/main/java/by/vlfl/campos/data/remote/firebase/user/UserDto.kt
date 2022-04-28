package by.vlfl.campos.data.remote.firebase.user

import by.vlfl.campos.domain.entity.User
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserDto(
    val id: String ?= "",
    val name: String ?= "",
    val birthDate: String ?= ""
)

fun UserDto.toDomainModel() = User(
    id = checkNotNull(this.id),
    name = checkNotNull(this.name),
    birthDate = checkNotNull(this.birthDate)
)
