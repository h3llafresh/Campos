package by.vlfl.campos.data.repository

import by.vlfl.campos.data.remote.firebase.PlaygroundsFirebaseRemoteApi
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val playgroundsFirebaseRemoteApi: PlaygroundsFirebaseRemoteApi): IUserRepository {
    override suspend fun getUserData(): Flow<User> =
        flow {
            emit(User(id = "", name = "", birthDate = ""))
        }
}