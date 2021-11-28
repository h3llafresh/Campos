package by.vlfl.campos.di.module

import by.vlfl.campos.data.remote.firebase.UsersFirebaseRemoteApi
import by.vlfl.campos.data.repository.UserRepository
import by.vlfl.campos.domain.repostitory.IUserRepository
import by.vlfl.campos.domain.usecase.GetUserProfileDataUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {
    @Singleton
    @Provides
    fun provideUserRepository(usersFirebaseRemoteApi: UsersFirebaseRemoteApi): IUserRepository =
        UserRepository(usersFirebaseRemoteApi)

    @Provides
    fun provideGetUserProfileDataUseCase(repository: IUserRepository): GetUserProfileDataUseCase =
        GetUserProfileDataUseCase(repository)
}