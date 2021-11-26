package by.vlfl.campos.domain.usecase

import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.repostitory.IUserRepository

class GetUserDataUseCase (private val userRepository: IUserRepository) {
    operator fun invoke(): User {
        return userRepository.getUserData()
    }
}