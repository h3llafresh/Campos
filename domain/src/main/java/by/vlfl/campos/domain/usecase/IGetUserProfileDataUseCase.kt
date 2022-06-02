package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User

interface IGetUserProfileDataUseCase {
    suspend operator fun invoke(userID: String): User?
}