package by.vlfl.campos.data.remote.firebase.user

import kotlinx.coroutines.flow.Flow

interface IUserRemoteDataSource {
    suspend fun getUserData(userID: String): UserDto?

    suspend fun registerUserData(userID: String, userName: String)

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String)

    suspend fun getUserCurrentPlayground(userID: String): Flow<UserCurrentPlaygroundDto?>

    suspend fun leaveCurrentGame(userID: String, playgroundID: String)
}