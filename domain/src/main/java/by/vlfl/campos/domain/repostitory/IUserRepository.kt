package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.User

interface IUserRepository {
    suspend fun getUserData(userID: String): User
}