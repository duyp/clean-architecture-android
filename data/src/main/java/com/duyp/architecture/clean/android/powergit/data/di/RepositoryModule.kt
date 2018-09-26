package com.duyp.architecture.clean.android.powergit.data.di

import com.duyp.architecture.clean.android.powergit.data.repositories.AuthenticationRepositoryImpl
import com.duyp.architecture.clean.android.powergit.data.repositories.UserRepositoryImpl
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds abstract fun userRepository(userRepositoryImpl: UserRepositoryImpl) : UserRepository

    @Binds abstract fun authenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository
}
