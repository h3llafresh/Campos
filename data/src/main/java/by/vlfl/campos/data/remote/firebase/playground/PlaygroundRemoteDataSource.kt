package by.vlfl.campos.data.remote.firebase.playground

import by.vlfl.campos.data.remote.firebase.playground.api.PlaygroundsFirebaseRemoteApi
import by.vlfl.campos.data.remote.firebase.user.UserDto
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class PlaygroundRemoteDataSource @Inject constructor(private val playgroundsFirebaseRemoteApi: PlaygroundsFirebaseRemoteApi): IPlaygroundRemoteDataSource {
    override suspend fun getPlaygrounds(): SharedFlow<PlaygroundDto> =
        playgroundsFirebaseRemoteApi.subscribeToPlaygroundsDataChangeEvent()

    override suspend fun getActivePlayers(playgroundID: String): SharedFlow<List<UserDto>> =
        playgroundsFirebaseRemoteApi.subscribeToActivePlayersDataChangeEvent(playgroundID)
}