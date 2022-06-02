package by.vlfl.campos.domain.usecase

interface IRegisterUserDataUseCase {
    suspend operator fun invoke(userID: String, userName: String)
}