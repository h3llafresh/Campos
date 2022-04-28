package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import kotlinx.coroutines.flow.Flow

class GetActivePlayersUseCase (
    private val playgroundsRepository: IPlaygroundRepository
) {
    suspend operator fun invoke(playgroundId: String): Flow<List<User>> = playgroundsRepository.getActivePlayers(playgroundId)
}