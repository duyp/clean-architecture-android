package com.duyp.architecture.clean.android.powergit.ui.features.login

import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.LoginUser
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
        private val mLoginUser: LoginUser,
        private val mGetUser: GetUser
) : BaseViewModel<LoginViewState, LoginIntent>() {

    override fun initState() = LoginViewState()

    override fun composeIntent(intentSubject: Observable<LoginIntent>) {
        // first populate last logged in username
        withState {
            if (lastLoggedInUsername == null)
                setState { copy(lastLoggedInUsername = Event(mGetUser.getLastLoggedInUsername())) }
        }

        // login intent
        addDisposable{
            intentSubject.subscribeOn(Schedulers.io())
                    // do nothing if login is in progress
                    .filter { !state().isLoading }
                    .doOnNext {
                        if (CommonUtil.isEmpty(it.username, it.password))
                            setState {
                                copy(errorMessage = Event("Please input username and password"))
                            }
                    }
                    .filter { !CommonUtil.isEmpty(it.username, it.password) }
                    .switchMapCompletable { intent ->
                        mLoginUser.login(intent.username!!, intent.password!!)
                                .doOnSubscribe {
                                    setState { copy(isLoading = true) }
                                }
                                .doOnError { throwable ->
                                    setState {
                                        copy(isLoading = false, errorMessage = Event(throwable.message ?: ""))
                                    }
                                }
                                .doOnComplete { setState { copy(loginSuccess = Event(Unit)) } }
                                .onErrorComplete()
                    }
                    .onErrorComplete()
                    .subscribe()
        }
    }
}

data class LoginViewState(
        val isLoading: Boolean = false,
        val errorMessage: Event<String>? = null,
        val loginSuccess: Event<Unit>? = null,
        val lastLoggedInUsername: Event<String?>? = null
)

data class LoginIntent(
        val username: String? = null,
        val password: String? = null
)