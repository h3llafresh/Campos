package by.vlfl.campos.data.repository

import by.vlfl.campos.data.remote.firebase.UsersFirebaseRemoteApi
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val usersFirebaseRemoteApi: UsersFirebaseRemoteApi) : IUserRepository {
    override suspend fun getUserData(userID: String): User =
        usersFirebaseRemoteApi.getUserData(userID)
}