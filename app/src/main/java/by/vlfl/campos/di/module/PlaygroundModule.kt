package by.vlfl.campos.di.module

import by.vlfl.campos.data.remote.firebase.PlaygroundsFirebaseRemoteApi
import by.vlfl.campos.data.repository.PlaygroundRepository
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import by.vlfl.campos.domain.usecase.GetActivePlayersUseCase
import by.vlfl.campos.domain.usecase.GetPlaygroundsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlaygroundModule {

    @Singleton
    @Provides
    fun providePlaygroundRepository(playgroundsFirebaseRemoteApi: PlaygroundsFirebaseRemoteApi): IPlaygroundRepository =
        PlaygroundRepository(playgroundsFirebaseRemoteApi)

    @Provides
    fun provideGetPlaygroundsUseCase(repository: IPlaygroundRepository): GetPlaygroundsUseCase = GetPlaygroundsUseCase(repository)

    @Provides
    fun provideGetActivePlayersUseCase(repository: IPlaygroundRepository): GetActivePlayersUseCase = GetActivePlayersUseCase(repository)
}