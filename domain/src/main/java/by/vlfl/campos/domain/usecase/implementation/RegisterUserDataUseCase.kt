package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.IRegisterUserDataUseCase

class RegisterUserDataUseCase(private val userRepository: IUserRepository) : IRegisterUserDataUseCase {
    override suspend fun invoke(userID: String, userName: String) = userRepository.registerUserData(userID, userName)
}