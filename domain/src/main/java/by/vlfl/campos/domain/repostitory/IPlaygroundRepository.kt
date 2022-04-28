package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IPlaygroundRepository {
    suspend fun getPlaygrounds(): Flow<Playground>

    suspend fun getActivePlayers(playgroundID: String): Flow<List<User>>
}