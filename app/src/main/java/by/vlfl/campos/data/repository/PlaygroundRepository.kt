package by.vlfl.campos.data.repository

import by.vlfl.campos.data.remote.firebase.PlaygroundsFirebaseRemoteApi
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import kotlinx.coroutines.flow.SharedFlow

class PlaygroundRepository(private val playgroundsFirebaseRemoteApi: PlaygroundsFirebaseRemoteApi) : IPlaygroundRepository {

    override suspend fun getPlaygrounds(): SharedFlow<Playground> = playgroundsFirebaseRemoteApi.subscribeToPlaygroundsDataChangeEvent()

    override suspend fun getActivePlayers(playgroundID: String): SharedFlow<List<User>> =
        playgroundsFirebaseRemoteApi.subscribeToActivePlayersDataChangeEvent(playgroundID)
}