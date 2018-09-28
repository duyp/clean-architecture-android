package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
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

    override fun createUseCase(): CheckUser {
        return CheckUser(mAuthenticationRepository)
    }

    @Test
    fun checkUser_hasUserAuthentication() {
        whenever(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.just("token"))

        mUsecase.isLoggedIn("user")
                .test()
                .assertValue { it }
    }

    @Test
    fun checkUser_noUserAuthentication() {
        whenever(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.empty())

        mUsecase.isLoggedIn("user1")
                .test()
                .assertValue { !it }
    }

    @Test
    fun checkUser_errorShouldReturnFalse() {
        whenever(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.error(Exception("Error")))

        mUsecase.isLoggedIn("user2")
                .test()
                .assertValue { !it }
    }

    @Test
    fun hasLoggedInUser_noCurrentUser() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
        verify(mAuthenticationRepository, times(0)).getAuthentication(anyString())
    }

    @Test
    fun hasLoggedInUser_emptyCurrentUser() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("")

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
        verify(mAuthenticationRepository, times(0)).getAuthentication(anyString())
    }

    @Test
    fun hasLoggedInUser_hasCurrentUser_noAuth() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("user")
        whenever(mAuthenticationRepository.getAuthentication("user")).thenReturn(Maybe.just("abcd"))

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { it }
    }

    @Test
    fun hasLoggedInUser_hasCurrentUser_getAuthError() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("user")
        whenever(mAuthenticationRepository.getAuthentication("user")).thenReturn(Maybe.error(Exception()))

        mUsecase.hasLoggedInUser()
                .test()
                .assertValue { !it }
    }
}