package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.IGetUserProfileDataUseCase

class GetUserProfileDataUseCase (private val userRepository: IUserRepository) : IGetUserProfileDataUseCase {
    override suspend fun invoke(userID: String): User? = userRepository.getUserData(userID)
}