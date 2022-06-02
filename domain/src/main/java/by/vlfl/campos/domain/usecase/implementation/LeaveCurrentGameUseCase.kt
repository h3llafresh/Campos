package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.ILeaveCurrentGameUseCase

class LeaveCurrentGameUseCase(private val userRepository: IUserRepository) : ILeaveCurrentGameUseCase {
    override suspend fun invoke(userID: String, playgroundID: String) = userRepository.leaveCurrentGame(userID, playgroundID)
}