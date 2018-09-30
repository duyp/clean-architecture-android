package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock

class LoginUserTest : UseCaseTest<LoginUser> () {

    @Mock private lateinit var mUserRepository: UserRepository

    @Mock private lateinit var mSettingRepository: SettingRepository

    override fun createUseCase(): LoginUser {
        return LoginUser(mUserRepository, mSettingRepository)
    }

    @Test fun login_success() {
        whenever(mUserRepository.login(any(), any())).thenAnswer {
            Single.just(UserEntity(1, it.getArgument(0)))
        }

        mUsecase.login("username", "")
                .test()
                .assertComplete()

        verify(mSettingRepository).setCurrentUsername("username")
        verify(mSettingRepository).setLastLoggedInUsername("username")
    }

    @Test fun login_error() {
        whenever(mUserRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.error(Exception("error")))

        mUsecase.login("duyp", "abcd")
                .test()
                .assertErrorMessage("error")
        verify(mSettingRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
        verify(mSettingRepository, times(0)).setLastLoggedInUsername(ArgumentMatchers.anyString())
    }
}