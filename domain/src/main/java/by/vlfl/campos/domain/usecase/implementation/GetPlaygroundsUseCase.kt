package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import by.vlfl.campos.domain.usecase.IGetPlaygroundsUseCase
import kotlinx.coroutines.flow.Flow

class GetPlaygroundsUseCase (private val playgroundRepository: IPlaygroundRepository) : IGetPlaygroundsUseCase {
    override suspend fun invoke(): Flow<Playground> = playgroundRepository.getPlaygrounds()
}