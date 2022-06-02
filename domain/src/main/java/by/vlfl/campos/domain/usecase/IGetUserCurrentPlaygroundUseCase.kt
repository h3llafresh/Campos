package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.UserCurrentPlayground
import kotlinx.coroutines.flow.Flow

interface IGetUserCurrentPlaygroundUseCase {
    suspend operator fun invoke(userID: String): Flow<UserCurrentPlayground?>
}