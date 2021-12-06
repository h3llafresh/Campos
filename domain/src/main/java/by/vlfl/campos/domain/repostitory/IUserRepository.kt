package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.User

interface IUserRepository {
    suspend fun getUserData(userID: String): User

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String)
}