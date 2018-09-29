package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class GetUserTest: UseCaseTest<GetUser>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mCheckUser: CheckUser

    @Mock private lateinit var mSettingRepository: SettingRepository

    override fun createUseCase(): GetUser {
        return GetUser(mAuthenticationRepository, mCheckUser, mSettingRepository)
    }

    @Test fun getCurrentLoggedInUsername() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 1")
        whenever(mCheckUser.isLoggedIn("user 1")).thenReturn(Single.just(true))

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertValue { it == "user 1" }
                .assertComplete()
    }

}