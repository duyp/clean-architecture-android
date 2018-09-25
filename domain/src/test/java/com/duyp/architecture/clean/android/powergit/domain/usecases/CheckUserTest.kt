package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`

class CheckUserTest : UseCaseTest<CheckUser>() {

    @Mock
    private lateinit var mAuthenticationRepository: AuthenticationRepository

    override fun createUseCase(): CheckUser {
        return CheckUser(mAuthenticationRepository)
    }

    @Test
    fun checkUser_hasUserAuthentication() {
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.just("token"))

        mUsecase.isLoggedIn("user")
                .test()
                .assertValue { it }
    }

    @Test
    fun checkUser_noUserAuthentication() {
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.empty())

        mUsecase.isLoggedIn("user1")
                .test()
                .assertValue { !it }
    }

    @Test
    fun checkUser_errorShouldReturnFalse() {
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.error(Exception("Error")))

        mUsecase.isLoggedIn("user2")
                .test()
                .assertValue { !it }
    }
}