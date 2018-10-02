package com.duyp.architecture.clean.android.powergit.ui.features.main

import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(
): BaseViewModel<MainViewState, MainIntent>() {

    override fun initState() = MainViewState()

    override fun composeIntent(intentSubject: Observable<MainIntent>) {
        addDisposable {
            intentSubject.ofType(MainIntent.OnPageSelected::class.java)
                    .doOnNext { setState { copy(currentPage = it.page) } }
                    .subscribe()
        }
    }
}

data class MainViewState(
        val currentPage: Int = 0
)

sealed class MainIntent {
    data class OnPageSelected(val page: Int): MainIntent()
}