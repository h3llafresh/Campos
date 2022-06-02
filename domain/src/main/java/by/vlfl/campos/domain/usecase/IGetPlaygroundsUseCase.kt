package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.Playground
import kotlinx.coroutines.flow.Flow

interface IGetPlaygroundsUseCase {
    suspend operator fun invoke(): Flow<Playground>
}