package by.vlfl.campos.di.module.user

import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.implementation.*
import dagger.Module
import dagger.Provides

@Module
class UserModule {
    @Provides
    fun provideGetUserProfileDataUseCase(repository: IUserRepository) = GetUserProfileDataUseCase(repository)

    @Provides
    fun provideRegisterUserDataUseCase(repository: IUserRepository) = RegisterUserDataUseCase(repository)

    @Provides
    fun provideGetUserCurrentPlaygroundUseCase(repository: IUserRepository) = GetUserCurrentPlaygroundUseCase(repository)

    @Provides
    fun provideCheckInCurrentUserUseCase(repository: IUserRepository) = CheckInCurrentUserUseCase(repository)

    @Provides
    fun provideLeaveCurrentGameUserUseCase(repository: IUserRepository) = LeaveCurrentGameUseCase(repository)
}