package by.vlfl.campos.domain.usecase

interface ILeaveCurrentGameUseCase {
    suspend operator fun invoke(userID: String, playgroundID: String)
}