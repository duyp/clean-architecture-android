package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock


class GetAuthenticationTest : UseCaseTest<GetAuthentication>() {

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mSettingRepository: SettingRepository

    override fun createUseCase(): GetAuthentication {
        return GetAuthentication(mAuthenticationRepository, mSettingRepository)
    }

    @Test fun getAuthentication_noCurrentUser() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn(null)

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()

        verify(mAuthenticationRepository, times(0)).getAuthentication(ArgumentMatchers.anyString())
    }

    @Test fun getAuthentication_hasCurrentUser_hasToken() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 1")
        whenever(mAuthenticationRepository.getAuthentication(any())).thenReturn("token")

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertValue { it == "token" }
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 1")
    }

    @Test fun getAuthentication_hasCurrentUser_noToken() {
        whenever(mSettingRepository.getCurrentUsername()).thenReturn("user 2")
        whenever(mAuthenticationRepository.getAuthentication(any())).thenReturn(null)

        mUsecase.getCurrentUserAuthentication()
                .test()
                .assertNoValues()
                .assertComplete()
        verify(mAuthenticationRepository).getAuthentication("user 2")
    }
}