package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import by.vlfl.campos.domain.usecase.IGetActivePlayersUseCase
import kotlinx.coroutines.flow.Flow

class GetActivePlayersUseCase (private val playgroundsRepository: IPlaygroundRepository) : IGetActivePlayersUseCase {
    override suspend fun invoke(playgroundId: String): Flow<List<User>> = playgroundsRepository.getActivePlayers(playgroundId)
}