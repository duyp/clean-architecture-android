package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.User
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*

class LoginUserTest : UseCaseTest<LoginUser> () {

    @Mock private lateinit var mUserRepository: UserRepository

    override fun createUseCase(): LoginUser {
        return LoginUser(mUserRepository)
    }

    @Test fun login_success() {
        `when`(mUserRepository.login(anyString(), anyString())).thenAnswer {
            Single.just(User(1, it.getArgument(0)))
        }

        mUsecase.login("username", "")
                .test()
                .assertComplete()

        verify(mUserRepository).setCurrentUsername("username")
        verify(mUserRepository).setLastLoggedInUsername("username")
    }

    @Test fun login_error() {
        `when`(mUserRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.error(Exception("error")))

        mUsecase.login("duyp", "abcd")
                .test()
                .assertErrorMessage("error")
        verify(mUserRepository, times(0)).setCurrentUsername(ArgumentMatchers.anyString())
        verify(mUserRepository, times(0)).setLastLoggedInUsername(ArgumentMatchers.anyString())
    }
}