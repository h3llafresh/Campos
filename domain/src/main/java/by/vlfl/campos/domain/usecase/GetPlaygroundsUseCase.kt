package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.repostitory.IPlaygroundRepository

class GetPlaygroundsUseCase(
    private val playgroundRepository: IPlaygroundRepository
) {
    operator fun invoke(): List<Playground> {
        return playgroundRepository.getPlaygrounds()
    }
}