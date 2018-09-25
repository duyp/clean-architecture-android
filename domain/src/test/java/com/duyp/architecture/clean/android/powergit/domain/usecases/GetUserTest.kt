package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

class GetUserTest: UseCaseTest<GetUser>() {

    @Mock private lateinit var mUserRepository: UserRepository

    @Mock private lateinit var mCheckUser: CheckUser

    override fun createUseCase(): GetUser {
        return GetUser(mUserRepository, mCheckUser)
    }

    @Test fun getCurrentLoggedInUsername() {
        `when`(mUserRepository.getCurrentUsername()).thenReturn("user 1")
        `when`(mCheckUser.isLoggedIn("user 1")).thenReturn(Single.just(true))

        mUsecase.getCurrentLoggedInUsername()
                .test()
                .assertValue { it == "user 1" }
                .assertComplete()
    }

}