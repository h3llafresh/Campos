package by.vlfl.campos.di.module.playground

import by.vlfl.campos.data.remote.firebase.playground.IPlaygroundRemoteDataSource
import by.vlfl.campos.data.remote.firebase.playground.PlaygroundRemoteDataSource
import by.vlfl.campos.data.repository.PlaygroundRepository
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import by.vlfl.campos.domain.usecase.IGetActivePlayersUseCase
import by.vlfl.campos.domain.usecase.IGetPlaygroundsUseCase
import by.vlfl.campos.domain.usecase.implementation.GetActivePlayersUseCase
import by.vlfl.campos.domain.usecase.implementation.GetPlaygroundsUseCase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PlaygroundBindModule {
    @Singleton
    @Binds
    fun bindPlaygroundRepository(playgroundRepository: PlaygroundRepository): IPlaygroundRepository

    @Singleton
    @Binds
    fun bindPlaygroundRemoteDataSource(playgroundRemoteDataSource: PlaygroundRemoteDataSource): IPlaygroundRemoteDataSource

    @Binds
    fun provideGetPlaygroundsUseCase(getPlaygroundsUseCase: GetPlaygroundsUseCase): IGetPlaygroundsUseCase

    @Binds
    fun provideGetActivePlayersUseCase(getActivePlayersUseCase: GetActivePlayersUseCase): IGetActivePlayersUseCase
}