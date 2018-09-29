package com.duyp.architecture.clean.android.powergit.ui.features.main

import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(
): BaseViewModel<MainViewState, MainIntent>() {

    override fun initState() = MainViewState()

    override fun composeIntent(intentSubject: Observable<MainIntent>) {

    }
}

data class MainViewState(
        val value: Int? = 1
)

interface MainIntent