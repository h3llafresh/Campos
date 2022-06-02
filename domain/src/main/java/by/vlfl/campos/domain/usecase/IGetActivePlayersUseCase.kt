package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IGetActivePlayersUseCase {
    suspend operator fun invoke(playgroundId: String): Flow<List<User>>
}