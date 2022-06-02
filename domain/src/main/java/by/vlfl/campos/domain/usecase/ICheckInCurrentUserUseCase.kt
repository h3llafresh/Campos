package by.vlfl.campos.domain.usecase

interface ICheckInCurrentUserUseCase {
    suspend operator fun invoke(userID: String, playgroundID: String, playgroundName: String)
}