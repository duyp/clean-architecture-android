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
import junit.framework.Assert
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
        // just forward call to authentication repository
        mUserRepository.logout("user")
                .test()
                .assertComplete()
        verify(mAuthenticationRepository).logout("user")
    }

    @Test
    fun getUser() {

    }

    @Test
    fun getLastLoggedInUsername() {
        whenever(mSharedPreferences.getString(any(), anyOrNull())).thenReturn("user 1")
        Assert.assertEquals("user 1", mUserRepository.getLastLoggedInUsername())
    }

    @Test
    fun setLastLoggedInUsername() {
        val editor = mock<SharedPreferences.Editor>()
        whenever(mSharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putString(any(), anyOrNull())).thenReturn(editor)
        mUserRepository.setLastLoggedInUsername("user 2")
        verify(editor).putString(any(), eq("user 2"))
        verify(editor).apply()
    }

    @Test
    fun setLastLoggedInUsername_nullShouldRemove() {
        val editor = mock<SharedPreferences.Editor>()
        whenever(mSharedPreferences.edit()).thenReturn(editor)
        whenever(editor.remove(any())).thenReturn(editor)
        mUserRepository.setLastLoggedInUsername(null)
        verify(editor).remove(any())
        verify(editor).apply()
    }
}