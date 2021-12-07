package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.repostitory.IUserRepository

class LeaveCurrentGameUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userID: String, playgroundID: String) = userRepository.leaveCurrentGame(userID, playgroundID)
}