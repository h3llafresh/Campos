package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import kotlinx.coroutines.flow.SharedFlow

interface IPlaygroundRepository {
    suspend fun getPlaygrounds(): SharedFlow<Playground>

    suspend fun getActivePlayers(playgroundId: String): SharedFlow<List<User>>
}