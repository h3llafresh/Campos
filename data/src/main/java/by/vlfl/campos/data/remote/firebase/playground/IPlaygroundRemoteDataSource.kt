package by.vlfl.campos.data.remote.firebase.playground

import by.vlfl.campos.data.remote.firebase.user.UserDto
import kotlinx.coroutines.flow.SharedFlow

interface IPlaygroundRemoteDataSource {

    suspend fun getPlaygrounds(): SharedFlow<PlaygroundDto>

    suspend fun getActivePlayers(playgroundID: String): SharedFlow<List<UserDto>>
}