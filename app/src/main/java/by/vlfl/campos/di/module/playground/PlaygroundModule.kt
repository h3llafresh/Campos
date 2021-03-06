package by.vlfl.campos.di.module.playground

import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import by.vlfl.campos.domain.usecase.implementation.GetActivePlayersUseCase
import by.vlfl.campos.domain.usecase.implementation.GetPlaygroundsUseCase
import dagger.Module
import dagger.Provides

@Module
class PlaygroundModule {

    @Provides
    fun provideGetPlaygroundsUseCase(repository: IPlaygroundRepository) = GetPlaygroundsUseCase(repository)

    @Provides
    fun provideGetActivePlayersUseCase(repository: IPlaygroundRepository) = GetActivePlayersUseCase(repository)
}