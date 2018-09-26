package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*


class GetAuthenticationTest : UseCaseTest<GetAuthentication>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mUserRepository: UserRepository

    override fun createUseCase(): GetAuthentication {
        return GetAuthentication(mAuthenticationRepository)
    }

    @Test fun getAuthentication_noCurrentUser() {
        `when`(mAuthenticationRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()

        verify(mAuthenticationRepository, times(0)).getAuthentication(ArgumentMatchers.anyString())
    }

    @Test fun getAuthentication_hasCurrentUser_hasToken() {
        `when`(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 1")
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.just("token"))

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertValue { it == "token" }
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 1")
    }

    @Test fun getAuthentication_hasCurrentUser_noToken() {
        `when`(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 2")
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.empty())

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 2")
    }

    @Test fun getAuthentication_hasCurrentUser_tokenError_shouldCompleteWithoutValue() {
        `when`(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 3")
        `when`(mAuthenticationRepository.getAuthentication(anyString())).thenReturn(Maybe.error(Exception("error")))

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertNoErrors()
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 3")
    }
}