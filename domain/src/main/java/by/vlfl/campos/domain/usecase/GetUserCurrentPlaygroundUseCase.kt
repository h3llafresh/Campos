package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.domain.repostitory.IUserRepository
import kotlinx.coroutines.flow.Flow

class GetUserCurrentPlaygroundUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String): Flow<UserCurrentPlayground?> = userRepository.getUserCurrentPlayground(userID)
}