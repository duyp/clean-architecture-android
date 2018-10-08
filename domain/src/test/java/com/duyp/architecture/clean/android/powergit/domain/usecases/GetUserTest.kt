package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class GetUserTest: UseCaseTest<GetUser>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mCheckUser: CheckUser

    @Mock private lateinit var mSettingRepository: SettingRepository

    @Mock private lateinit var mUserRepository: UserRepository

    override fun createUseCase(): GetUser {
        return GetUser(mAuthenticationRepository, mCheckUser, mSettingRepository, mUserRepository)
    }

    @Test fun getCurrentLoggedInUsername_hasCurrentUser_isLoggedIn() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 1")
        whenever(mCheckUser.isLoggedIn("user 1")).thenReturn(Single.just(true))

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertValue { it == "user 1" }
                .assertComplete()
    }

    @Test fun getCurrentLoggedInUsername_hasCurrentUser_notLoggedIn_shouldThrowException() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 2")
        whenever(mCheckUser.isLoggedIn("user 2")).thenReturn(Single.just(false))

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertError(AuthenticationException::class.java)
    }

    @Test fun getCurrentLoggedInUsername_hasCurrentUser_checkLoginError_shouldAlsoThrowAuthenticationException() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 2")
        whenever(mCheckUser.isLoggedIn(any())).thenReturn(Single.error(Exception()))

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertError(AuthenticationException::class.java)
    }

    @Test fun getCurrentLoggedInUsername_noCurrentUser_shouldThrowError() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertError(AuthenticationException::class.java)
        verifyZeroInteractions(mCheckUser)
    }

    @Test fun getCurrentLoggedInUser_hasCurrentUser() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 1")
        whenever(mCheckUser.isLoggedIn(any())).thenReturn(Single.just(true))
        whenever(mUserRepository.getUser(any())).thenReturn(Flowable.just(UserEntity(id = 10, login = "duyp")))

        mUsecase.getCurrentLoggedInUser()
                .test()
                .assertValue { it.id == 10L && it.login == "duyp" }
        verify(mUserRepository).getUser("user 1")
    }

    @Test fun getCurrentLoggedInUser_noCurrentUser() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.getCurrentLoggedInUser()
                .test()
                .assertError(AuthenticationException::class.java)
        verifyZeroInteractions(mUserRepository)
    }

}