package com.duyp.architecture.clean.android.powergit.ui.features.splash


import ViewModelTest
import com.duyp.architecture.clean.android.powergit.domain.usecases.CheckUser
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class SplashViewModelTest : ViewModelTest<SplashState, Any, SplashViewModel>() {

    @Mock internal lateinit var mCheckUser: CheckUser

    override fun createViewModel(): SplashViewModel {
        return SplashViewModel(mCheckUser)
    }

    @Test
    fun hasLoggedInUser_shouldNavigateMain() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.just(true))
        processIntents()

        viewState().assertValue { navigation!!.peekContent() == Navigation.MAIN }
    }

    @Test
    fun noLoggedInUser_shouldNavigateLogin() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.just(false))
        processIntents()

        viewState().assertValue { navigation!!.peekContent() == Navigation.LOGIN }
    }

    @Test
    fun checkUserError_shouldNavigateLogin() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.error(Exception()))
        processIntents()

        viewState().assertValue { navigation!!.peekContent() == Navigation.LOGIN }
    }

}