package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.repostitory.IUserRepository
import kotlinx.coroutines.flow.Flow

class GetUserCurrentPlayground(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String): Flow<Playground?> = userRepository.getUserCurrentPlayground(userID)
}