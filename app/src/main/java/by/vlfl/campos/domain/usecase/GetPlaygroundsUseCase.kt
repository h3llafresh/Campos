package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository
import kotlinx.coroutines.flow.SharedFlow

class GetPlaygroundsUseCase (
    private val playgroundRepository: IPlaygroundRepository
) {
    suspend operator fun invoke(): SharedFlow<Playground> = playgroundRepository.getPlaygrounds()
}