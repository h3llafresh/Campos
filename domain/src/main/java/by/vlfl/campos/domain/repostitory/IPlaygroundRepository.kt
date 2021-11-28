package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.Playground
import kotlinx.coroutines.flow.SharedFlow

interface IPlaygroundRepository {
    suspend fun getPlaygrounds(): SharedFlow<Playground>
}