package com.duyp.architecture.clean.android.powergit.data.repositories

import android.content.SharedPreferences
import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.database.UserDao
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.duyp.architecture.clean.android.powergit.data.utils.ApiHelper
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.nhaarman.mockitokotlin2.*
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
    internal lateinit var mAuthenticationRepository: AuthenticationRepository

    @Mock
    internal lateinit var mSharedPreferences: SharedPreferences

    internal lateinit var mApiHelper: ApiHelper

    private lateinit var mUserRepository: UserRepository

    @Before
    fun setup() {
        mApiHelper = mock {
            on { getBasicAuth(any(), any()) }.doAnswer { invocationOnMock ->
                "Basic " + invocationOnMock.arguments[0] + "," + invocationOnMock.arguments[1]
            }
        }
        mUserRepository = UserRepositoryImpl(mUserService, mUserDao, mAuthenticationRepository, mSharedPreferences, mApiHelper)
    }

    @Test
    fun login_success_shouldStoreUserData() {
        val user = UserApiData(1, "duyp")
        whenever(mUserService.login(any())).thenReturn(Single.just(user))

        mUserRepository.login("duyp", "abcd")
                .test()
                .assertValue { it.id == 1L && it.login == "duyp" }
                .assertComplete()
        verify(mUserDao).insert(argThat { id == 1L && login == "duyp" })
        verify(mAuthenticationRepository).addOrUpdateUser(eq("duyp"), eq("abcd"), eq("Basic duyp,abcd"))
    }

    @Test
    fun login_error() {
        whenever(mUserService.login(any())).thenReturn(Single.error(Exception()))

        mUserRepository.login("a", "a")
                .test()
                .assertNoValues()
                .assertError(Exception::class.java)
        verifyZeroInteractions(mAuthenticationRepository)
        verifyZeroInteractions(mUserDao)
    }

    @Test
    fun logout() {
    }

    @Test
    fun getUser() {
    }

    @Test
    fun getLastLoggedInUsername() {
    }

    @Test
    fun setLastLoggedInUsername() {
    }
}