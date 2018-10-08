package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.database.UserDao
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @Mock
    internal lateinit var mUserService: UserService

    @Mock
    internal lateinit var mUserDao: UserDao

    @Mock
    private lateinit var mUserRepository: UserRepository

    @Before
    fun setup() {
        mUserRepository = UserRepositoryImpl(mUserService, mUserDao)
    }

    @Test
    fun login_success_shouldStoreUserData() {
        val user = UserApiData(1, "duyp")
        whenever(mUserService.login(any())).thenReturn(Single.just(user))

        mUserRepository.login("Basic abcd")
                .test()
                .assertValue { it.id == 1L && it.login == "duyp" }
                .assertComplete()
        verify(mUserDao).insert(argThat { id == 1L && login == "duyp" })
    }

    @Test
    fun login_error() {
        whenever(mUserService.login(any())).thenReturn(Single.error(Exception()))

        mUserRepository.login("12345")
                .test()
                .assertNoValues()
                .assertError(Exception::class.java)
        verifyZeroInteractions(mUserDao)
    }

    @Test
    fun logout() {
        mUserRepository.logout("user")
                .test()
                .assertComplete()
    }

    @Test
    fun getUser() {
        whenever(mUserDao.getUser(any())).thenReturn(Flowable.just(UserLocalData(
                id = 100, login = "duyp", name = "Duy Pham")))

        mUserRepository.getUser("duyp")
                .test()
                .assertValue { it.id == 100L && it.login == "duyp" && it.name == "Duy Pham" }
        verify(mUserDao).getUser("duyp")
    }
}