package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.repostitory.IUserRepository

class RegisterUserDataUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String, userName: String) {
        return userRepository.registerUserData(userID, userName)
    }
}