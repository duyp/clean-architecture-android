package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*

class LogoutUserTest : UseCaseTest<LogoutUser>() {

    @Mock private lateinit var mUserRepository: UserRepository

    override fun createUseCase(): LogoutUser {
        return LogoutUser(mUserRepository)
    }

    @Test fun logout_noCurrentUser_shouldComplete() {
        `when`(mUserRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository, times(0)).logout(ArgumentMatchers.anyString())
        verify(mUserRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_success() {
        `when`(mUserRepository.getCurrentUsername()).thenReturn("username")
        `when`(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.complete())

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mUserRepository).setCurrentUsername(null)
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_error_shouldCompleteWithoutThrow() {
        `when`(mUserRepository.getCurrentUsername()).thenReturn("username")
        `when`(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.error(Exception()))

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mUserRepository).setCurrentUsername(null)
    }
}