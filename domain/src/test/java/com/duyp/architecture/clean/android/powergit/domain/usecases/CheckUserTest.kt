package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.AuthenticationEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock

class CheckUserTest : UseCaseTest<CheckUser>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mSettingRepository: SettingRepository

    @Mock private lateinit var mLoginUser: LoginUser

    override fun createUseCase(): CheckUser {
        return CheckUser(mAuthenticationRepository, mSettingRepository, mLoginUser)
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
        whenever(mAuthenticationRepository.getAuthentication("")).thenReturn(null)

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
    }

    @Test
    fun hasLoggedInUser_hasCurrentUser_noAuth() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user")
        whenever(mAuthenticationRepository.getAuthentication("user")).thenReturn("abcd")

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { it }
    }

    @Test
    fun checkUser_noLoggedInUser_noAuthenticatedAccounts() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)
        whenever(mAuthenticationRepository.getAuthenticatedAccounts()).thenReturn(Single.just(emptyList()))

        mUsecase.checkCurrentUser()
                .test()
                .assertValue { !it }
                .assertComplete()

        verifyZeroInteractions(mLoginUser)
        verify(mSettingRepository, times(0)).setLastLoggedInUsername(any())
    }

    @Test
    fun checkUser_noLoggedInUser_hasAuthenticatedAccounts_loginError() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)
        whenever(mAuthenticationRepository.getAuthenticatedAccounts()).thenReturn(Single.just(listOf(AuthenticationEntity(
                username = "duyp", password = "1234", token = "abcd"
        ))))
        whenever(mLoginUser.login("duyp", "1234")).thenReturn(Completable.error(AuthenticationException()))

        mUsecase.checkCurrentUser()
                .test()
                .assertValue { !it }
                .assertComplete()

        verify(mSettingRepository).setLastLoggedInUsername("duyp")
    }

    @Test
    fun checkUser_noLoggedInUser_hasAuthenticatedAccounts_loginSuccess() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)
        whenever(mAuthenticationRepository.getAuthenticatedAccounts()).thenReturn(Single.just(listOf(AuthenticationEntity(
                username = "duyp1", password = "12345", token = "abcde"
        ))))
        whenever(mLoginUser.login("duyp1", "12345")).thenReturn(Completable.complete())

        mUsecase.checkCurrentUser()
                .test()
                .assertValue { it }
                .assertComplete()

        verify(mSettingRepository).setLastLoggedInUsername("duyp1")
    }
}