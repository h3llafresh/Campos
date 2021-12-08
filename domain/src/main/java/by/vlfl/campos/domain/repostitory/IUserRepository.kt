package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserData(userID: String): User?

    suspend fun registerUserData(userID: String, userName: String)

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String)

    suspend fun getUserCurrentPlayground(userID: String): Flow<Playground?>

    suspend fun leaveCurrentGame(userID: String, playgroundID: String)
}