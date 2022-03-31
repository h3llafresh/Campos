package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import kotlinx.coroutines.flow.SharedFlow

class GetActivePlayersUseCase (
    private val playgroundsRepository: IPlaygroundRepository
) {
    suspend operator fun invoke(playgroundId: String): SharedFlow<List<User>> = playgroundsRepository.getActivePlayers(playgroundId)
}