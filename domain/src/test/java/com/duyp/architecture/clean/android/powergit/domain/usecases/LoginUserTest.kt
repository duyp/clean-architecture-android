package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.AndroidInteractor
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock

class LoginUserTest : UseCaseTest<LoginUser> () {

    @Mock private lateinit var mUserRepository: UserRepository

    @Mock private lateinit var mSettingRepository: SettingRepository

    @Mock private lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock private lateinit var mAndroidInteractor: AndroidInteractor

    override fun createUseCase(): LoginUser {
        mAndroidInteractor = mock {
            on { getBasicAuth(any(), any()) }.doAnswer { invocationOnMock ->
                "Basic " + invocationOnMock.arguments[0] + "," + invocationOnMock.arguments[1]
            }
        }
        return LoginUser(mUserRepository, mSettingRepository, mAndroidInteractor, mAuthenticationRepository)
    }

    @Test fun login_success() {
        whenever(mUserRepository.login(any())).thenAnswer {
            Single.just(UserEntity(id = 1, login = "login"))
        }

        mUsecase.login("username", "abcd")
                .test()
                .assertComplete()

        verify(mSettingRepository).setCurrentUsername("username")
        verify(mSettingRepository).setLastLoggedInUsername("username")
        verify(mAuthenticationRepository).addOrUpdateUser("username", "abcd", "Basic username,abcd")
    }

    @Test fun login_error() {
        whenever(mUserRepository.login(ArgumentMatchers.anyString()))
                .thenReturn(Single.error(Exception("error")))

        mUsecase.login("duyp", "abcd")
                .test()
                .assertErrorMessage("error")
        verify(mSettingRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
        verify(mSettingRepository, times(0)).setLastLoggedInUsername(ArgumentMatchers.anyString())
        verifyZeroInteractions(mAuthenticationRepository)
    }
}