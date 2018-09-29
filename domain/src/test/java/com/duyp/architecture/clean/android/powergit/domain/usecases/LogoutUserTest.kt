package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
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

    @Mock private lateinit var mSettingRepository: SettingRepository

    override fun createUseCase(): LogoutUser {
        return LogoutUser(mUserRepository, mSettingRepository)
    }

    @Test fun logout_noCurrentUser_shouldComplete() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository, times(0)).logout(ArgumentMatchers.anyString())
        verify(mSettingRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_success() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("username")
        whenever(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.complete())

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mSettingRepository).setCurrentUsername(null)
    }

    @Test fun logout_hasCurrentUser_shouldDoLogout_error_shouldCompleteWithoutThrow() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("username")
        whenever(mUserRepository.logout(ArgumentMatchers.anyString())).thenReturn(Completable.error(Exception()))

        mUsecase.logoutCurrentUser()
                .test()
                .assertComplete()

        verify(mUserRepository).logout("username")
        verify(mSettingRepository).setCurrentUsername(null)
    }
}