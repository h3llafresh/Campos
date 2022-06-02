package by.vlfl.campos.data.repository

import by.vlfl.campos.data.remote.firebase.user.IUserRemoteDataSource
import by.vlfl.campos.data.remote.firebase.user.toDomainModel
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.domain.repostitory.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userRemoteDataSource: IUserRemoteDataSource) : IUserRepository {
    override suspend fun getUserData(userID: String): User? =
        userRemoteDataSource.getUserData(userID)?.toDomainModel()

    override suspend fun registerUserData(userID: String, userName: String) {
        userRemoteDataSource.registerUserData(userID, userName)
    }

    override suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String) =
        userRemoteDataSource.checkInCurrentUser(userID, playgroundID, playgroundName)

    override suspend fun getUserCurrentPlayground(userID: String): Flow<UserCurrentPlayground?> =
        userRemoteDataSource.getUserCurrentPlayground(userID).map { userCurrentPlaygroundDto ->
            userCurrentPlaygroundDto?.toDomainModel()
        }

    override suspend fun leaveCurrentGame(userID: String, playgroundID: String) {
        userRemoteDataSource.leaveCurrentGame(userID, playgroundID)
    }
}