package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.repostitory.IUserRepository

class CheckInCurrentUserUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String, playgroundID: String, playgroundName: String) {
        userRepository.checkInCurrentUser(userID, playgroundID, playgroundName)
    }
}