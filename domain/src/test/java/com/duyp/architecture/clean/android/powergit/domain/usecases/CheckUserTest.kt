package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock

class CheckUserTest : UseCaseTest<CheckUser>() {

    @Mock
    private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock lateinit var mSettingRepository: SettingRepository

    override fun createUseCase(): CheckUser {
        return CheckUser(mAuthenticationRepository, mSettingRepository)
    }

    @Test
    fun checkUser_hasUserAuthentication() {
        whenever(mAuthenticationRepository.getAuthentication(anyString())).thenReturn("token")

        mUsecase.isLoggedIn("user")
                .test()
                .assertValue { it }
    }

    @Test
    fun checkUser_noUserAuthentication() {
        whenever(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(null)

        mUsecase.isLoggedIn("user1")
                .test()
                .assertValue { !it }
    }

    @Test
    fun hasLoggedInUser_noCurrentUser() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
        verify(mAuthenticationRepository, times(0)).getAuthentication(anyString())
    }

    @Test
    fun hasLoggedInUser_emptyCurrentUser() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("")

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
        verify(mAuthenticationRepository, times(0)).getAuthentication(anyString())
    }

    @Test
    fun hasLoggedInUser_hasCurrentUser_noAuth() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user")
        whenever(mAuthenticationRepository.getAuthentication("user")).thenReturn("abcd")

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { it }
    }
}