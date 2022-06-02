package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.ICheckInCurrentUserUseCase

class CheckInCurrentUserUseCase(private val userRepository: IUserRepository) : ICheckInCurrentUserUseCase {
    override suspend fun invoke(userID: String, playgroundID: String, playgroundName: String) =
        userRepository.checkInCurrentUser(userID, playgroundID, playgroundName)
}