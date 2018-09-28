package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock


class GetAuthenticationTest : UseCaseTest<GetAuthentication>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    override fun createUseCase(): GetAuthentication {
        return GetAuthentication(mAuthenticationRepository)
    }

    @Test fun getAuthentication_noCurrentUser() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()

        verify(mAuthenticationRepository, times(0)).getAuthentication(ArgumentMatchers.anyString())
    }

    @Test fun getAuthentication_hasCurrentUser_hasToken() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 1")
        whenever(mAuthenticationRepository.getAuthentication(any())).thenReturn(Maybe.just("token"))

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertValue { it == "token" }
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 1")
    }

    @Test fun getAuthentication_hasCurrentUser_noToken() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 2")
        whenever(mAuthenticationRepository.getAuthentication(any())).thenReturn(Maybe.empty())

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 2")
    }

    @Test fun getAuthentication_hasCurrentUser_tokenError_shouldCompleteWithoutValue() {
        whenever(mAuthenticationRepository.getCurrentUsername()).thenReturn("user 3")
        whenever(mAuthenticationRepository.getAuthentication(any())).thenReturn(Maybe.error(Exception("error")))

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertNoErrors()
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 3")
    }
}