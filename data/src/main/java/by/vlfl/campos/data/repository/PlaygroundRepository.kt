package by.vlfl.campos.data.repository

import by.vlfl.campos.data.remote.firebase.playground.IPlaygroundRemoteDataSource
import by.vlfl.campos.data.remote.firebase.playground.toDomainModel
import by.vlfl.campos.data.remote.firebase.user.toDomainModel
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaygroundRepository @Inject constructor(private val playgroundRemoteDataSource: IPlaygroundRemoteDataSource) : IPlaygroundRepository {

    override suspend fun getPlaygrounds(): Flow<Playground> =
        playgroundRemoteDataSource.getPlaygrounds().map {
            it.toDomainModel()
        }.flowOn(Dispatchers.Default)

    override suspend fun getActivePlayers(playgroundID: String): Flow<List<User>> =
        playgroundRemoteDataSource.getActivePlayers(playgroundID).map { activePlayersDto ->
            activePlayersDto.map {
                it.toDomainModel()
            }
        }.flowOn(Dispatchers.Default)
}