package com.duyp.architecture.clean.android.powergit.ui.features.splash


import ViewModelTest
import com.duyp.architecture.clean.android.powergit.domain.usecases.CheckUser
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class SplashViewModelTest : ViewModelTest<SplashState, SplashIntent, SplashViewModel>() {

    @Mock internal lateinit var mCheckUser: CheckUser

    override fun createViewModel(): SplashViewModel {
        return SplashViewModel(mCheckUser)
    }

    override fun setup() {
        super.setup()
        processIntents()
    }

    @Test
    fun hasLoggedInUser_shouldNavigateMain() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.just(true))
        intent(SplashIntent())

        viewState().assertValue { navigation!!.peekContent() == Navigation.MAIN }
    }

    @Test
    fun noLoggedInUser_shouldNavigateLogin() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.just(false))
        intent(SplashIntent())

        viewState().assertValue { navigation!!.peekContent() == Navigation.LOGIN }
    }

    @Test
    fun checkUserError_shouldNavigateLogin() {
        whenever(mCheckUser.hasLoggedInUser()).thenReturn(Single.error(Exception()))
        intent(SplashIntent())

        viewState().assertValue { navigation!!.peekContent() == Navigation.LOGIN }
    }

}