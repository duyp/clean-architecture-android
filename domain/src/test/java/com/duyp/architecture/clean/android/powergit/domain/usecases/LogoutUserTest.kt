package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock

class LogoutUserTest : UseCaseTest<LogoutUser>() {

    @Mock private lateinit var mUserRepository: UserRepository

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    override fun createUseCase(): LogoutUser {
        return LogoutUser(mUserRepository, mAuthenticationRepository)
    }

    @Test fun logout_noCurrentUser_shouldComplete() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository, times(0)).logout(ArgumentMatchers.anyString())
        verify(mAuthenticationRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_success() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("username")
        whenever(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.complete())

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mAuthenticationRepository).setCurrentUsername(null)
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_error_shouldCompleteWithoutThrow() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("username")
        whenever(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.error(Exception()))

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mAuthenticationRepository).setCurrentUsername(null)
    }
}