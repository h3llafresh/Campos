package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.User

interface IUserRepository {
    fun getUserData(): User
}