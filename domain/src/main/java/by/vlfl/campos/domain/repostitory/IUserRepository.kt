package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.entity.UserCurrentPlayground
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserData(userID: String): User?

    suspend fun registerUserData(userID: String, userName: String)

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String)

    suspend fun getUserCurrentPlayground(userID: String): Flow<UserCurrentPlayground?>

    suspend fun leaveCurrentGame(userID: String, playgroundID: String)
}