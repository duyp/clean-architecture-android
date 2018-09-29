package com.duyp.architecture.clean.android.powergit.ui.features.main

import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.entities.User
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.LogoutUser
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val mGetUser: GetUser,
        private val mLogoutUser: LogoutUser
): BaseViewModel<MainViewState, MainIntent>() {

    override fun initState() = MainViewState()

    override fun composeIntent(intentSubject: Observable<MainIntent>) {

        addDisposable {
            intentSubject.ofType(MainIntent.RefreshUser::class.java)
                    .subscribeOn(Schedulers.io())
                    .switchMap {
                        mGetUser.getCurrentUser()
                                .doOnNext { user ->
                                    setState { copy(user = user) }
                                }
                                .doOnError { _ ->
                                    setState { copy(user = null) }
                                }
                                .onErrorResumeNext(Flowable.empty())
                                .toObservable()
                    }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(MainIntent.LogoutIntent::class.java)
                    .subscribeOn(Schedulers.io())
                    .switchMapCompletable {
                        mLogoutUser.logoutCurrentUser()
                                .onErrorComplete()
                                .doOnComplete {
                                    setState { copy(refreshUser = Event(Unit)) }
                                }
                    }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(MainIntent.OpenUserProfile::class.java)
                    .subscribeOn(Schedulers.io())
                    .switchMapSingle {
                        mGetUser.getCurrentLoggedInUsername()
                                .onErrorReturnItem("")
                                .doOnSuccess { username ->
                                    if (username.isEmpty()) {
                                        setState { copy(navigation = Event(MainNavigation.LOGIN)) }
                                    } else {
                                        setState { copy(navigation = Event(MainNavigation.MY_PROFILE)) }
                                    }
                                }
                    }
                    .subscribe()
        }
    }
}

data class MainViewState(
        val user: User? = null,
        val navigation: Event<MainNavigation>? = null,
        val refreshUser: Event<Unit>? = null
)

interface MainIntent {
    class RefreshUser: MainIntent
    class LogoutIntent: MainIntent
    class OpenUserProfile: MainIntent
}

enum class MainNavigation {
    LOGIN,
    MY_PROFILE
}