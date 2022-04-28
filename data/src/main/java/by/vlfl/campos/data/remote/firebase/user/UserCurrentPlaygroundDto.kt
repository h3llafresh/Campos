package by.vlfl.campos.data.remote.firebase.user

import by.vlfl.campos.domain.entity.UserCurrentPlayground

data class UserCurrentPlaygroundDto(
    val id: String? = null,
    val name: String? = null
)

fun UserCurrentPlaygroundDto.toDomainModel() = UserCurrentPlayground(
    id = checkNotNull(this.id),
    playgroundName = checkNotNull(this.name)
)