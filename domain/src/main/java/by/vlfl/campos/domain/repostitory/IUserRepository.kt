package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserData(): Flow<User>
}