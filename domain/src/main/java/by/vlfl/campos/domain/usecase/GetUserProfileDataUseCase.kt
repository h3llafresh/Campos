package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository

class GetUserProfileDataUseCase (private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String): User? {
        return userRepository.getUserData(userID)
    }
}