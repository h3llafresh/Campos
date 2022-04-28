package by.vlfl.campos.data.remote.firebase.user

import by.vlfl.campos.data.remote.firebase.user.api.UsersFirebaseRemoteApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(private val usersFirebaseRemoteApi: UsersFirebaseRemoteApi) : IUserRemoteDataSource {
    override suspend fun getUserData(userID: String): UserDto? =
        usersFirebaseRemoteApi.getUserData(userID)

    override suspend fun registerUserData(userID: String, userName: String) {
        usersFirebaseRemoteApi.registerUserData(userID, userName)
    }

    override suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String) =
        usersFirebaseRemoteApi.checkInCurrentUser(userID, playgroundID, playgroundName)

    override suspend fun getUserCurrentPlayground(userID: String): Flow<UserCurrentPlaygroundDto?> =
        usersFirebaseRemoteApi.subscribeToUserCurrentPlayground(userID)

    override suspend fun leaveCurrentGame(userID: String, playgroundID: String) {
        usersFirebaseRemoteApi.leaveCurrentGame(userID, playgroundID)
    }
}