package com.duyp.architecture.clean.android.powergit.ui.features.login

import ViewModelTest
import com.duyp.architecture.clean.android.powergit.domain.usecases.LoginUser
import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class LoginViewModelTest : ViewModelTest<LoginViewState, LoginIntent, LoginViewModel>() {

    @Mock
    internal lateinit var mLoginUser: LoginUser

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel(mLoginUser)
    }

    override fun setup() {
        super.setup()
        processIntents()
    }

    @Test
    fun login_emptyUsername() {
        intent(LoginIntent(null, "abcd"))

        viewState().assertValue {
            !it.isLoading && it.errorMessage != null
        }
    }

    @Test
    fun login_emptyPassword() {
        intent(LoginIntent("duyp", ""))

        viewState().assertValue {
            !it.isLoading && it.errorMessage != null
        }
    }

    @Test
    fun login_bothUsernameAndPasswordEmpty() {
        intent(LoginIntent(null, ""))

        viewState().assertValue {
            !it.isLoading && it.errorMessage != null
        }
    }

    @Test
    fun login_error() {
        `when`(mLoginUser.login(anyString(), anyString())).thenReturn(Completable.error(Exception("login error")))
        intent(LoginIntent("duyp", "1234"))

        viewState().assertValue {
            !it.isLoading && it.errorMessage!!.peekContent() == "login error"
        }
        verify(mLoginUser).login("duyp", "1234")
    }

    @Test
    fun login_success() {
        `when`(mLoginUser.login(anyString(), anyString())).thenReturn(Completable.complete())
        intent(LoginIntent("duyp1", "12345"))

        viewState().assertValue {
            it.isLoading && it.errorMessage == null && it.loginSuccess != null
        }
        verify(mLoginUser).login("duyp1", "12345")
    }
}