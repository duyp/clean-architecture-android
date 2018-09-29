package com.duyp.architecture.clean.android.powergit.ui.features.splash

import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.usecases.CheckUser
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val mCheckUser: CheckUser
) : BaseViewModel<SplashState, Any>() {

    override fun composeIntent(intentSubject: Observable<Any>) {
           addDisposable {
               mCheckUser.hasLoggedInUser()
                       .onErrorReturnItem(false)
                       .subscribe { hasLoggedInUser ->
                           if (hasLoggedInUser)
                               setState { copy(navigation = Event(Navigation.MAIN)) }
                           else
                               setState { copy(navigation = Event(Navigation.LOGIN)) }
                       }
           }
    }

    override fun initState() = SplashState()

}

data class SplashState(
        val navigation: Event<Navigation>? = null
)

enum class Navigation {
    LOGIN,
    MAIN
}