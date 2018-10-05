package com.duyp.architecture.clean.android.powergit.ui.features.login

import ViewModelTest
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.LoginUser
import com.duyp.architecture.clean.android.powergit.utils.assertContent
import com.duyp.architecture.clean.android.powergit.utils.assertNotNullAndNotHandledYet
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mock

class LoginViewModelTest : ViewModelTest<LoginViewState, LoginIntent, LoginViewModel>() {

    @Mock
    internal lateinit var mLoginUser: LoginUser

    @Mock
    internal lateinit var mGetUser: GetUser

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel(mLoginUser, mGetUser)
    }

    @Test
    fun noLastLoggedInUsername() {
        whenever(mGetUser.getLastLoggedInUsername()).thenReturn(null)
        processIntents()

        viewState()
                .assertLastValue {
                    lastLoggedInUsername.assertNotNullAndNotHandledYet() && !isLoading && errorMessage == null
                }
                .noPrevious()
    }

    @Test
    fun hasLastLoggedInUsername() {
        whenever(mGetUser.getLastLoggedInUsername()).thenReturn("duyp")
        processIntents()

        viewState().assertLastValue { lastLoggedInUsername.assertContent("duyp") && !isLoading && errorMessage == null }
                .noPrevious()
    }

    @Test
    fun login_emptyUsername() {
        intent(LoginIntent(null, "abcd"))

        viewState().assertLastValue {
            !isLoading && errorMessage != null
        }
    }

    @Test
    fun login_emptyPassword() {
        intent(LoginIntent("duyp", ""))

        viewState().assertLastValue {
            !isLoading && errorMessage != null
        }
    }

    @Test
    fun login_bothUsernameAndPasswordEmpty() {
        intent(LoginIntent(null, ""))

        viewState().assertLastValue {
            !isLoading && errorMessage!!.peekContent().contains("username")
        }
    }

    @Test
    fun login_error() {
        whenever(mLoginUser.login(any(), any())).thenReturn(Completable.error(Exception("login error")))
        intent(LoginIntent("duyp", "1234"))

        viewState().assertLastValue { !isLoading && errorMessage.assertContent("login error") }
                .withPrevious { isLoading }

        verify(mLoginUser).login("duyp", "1234")
    }

    @Test
    fun login_success() {
        whenever(mLoginUser.login(any(), any())).thenReturn(Completable.complete())
        intent(LoginIntent("duyp1", "12345"))

        viewState().assertLastValue { errorMessage == null && loginSuccess != null }
                .withPrevious { isLoading }

        verify(mLoginUser).login("duyp1", "12345")
    }
}