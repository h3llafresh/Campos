package by.vlfl.campos.domain.usecase.implementation

import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.IGetUserCurrentPlaygroundUseCase
import kotlinx.coroutines.flow.Flow

class GetUserCurrentPlaygroundUseCase(private val userRepository: IUserRepository) : IGetUserCurrentPlaygroundUseCase {
    override suspend fun invoke(userID: String): Flow<UserCurrentPlayground?> = userRepository.getUserCurrentPlayground(userID)
}