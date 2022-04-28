package by.vlfl.campos.di.module.user

import by.vlfl.campos.data.remote.firebase.user.IUserRemoteDataSource
import by.vlfl.campos.data.remote.firebase.user.UserRemoteDataSource
import by.vlfl.campos.data.repository.UserRepository
import by.vlfl.campos.domain.repostitory.IUserRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UserBindModule {

    @Singleton
    @Binds
    fun bindUserRepository(userRepository: UserRepository): IUserRepository

    @Singleton
    @Binds
    fun bindUserRemoteDataSource(userRemoteDataSource: UserRemoteDataSource): IUserRemoteDataSource
}