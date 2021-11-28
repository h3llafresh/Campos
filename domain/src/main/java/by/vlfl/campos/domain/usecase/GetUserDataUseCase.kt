package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository
import kotlinx.coroutines.flow.Flow

class GetUserDataUseCase (private val userRepository: IUserRepository) {
    suspend operator fun invoke(): Flow<User> {
        return userRepository.getUserData()
    }
}