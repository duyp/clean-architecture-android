package com.duyp.architecture.clean.android.powergit.ui.features.test

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

data class ScreenState(
    val isLoading: Boolean,
    val isButtonEnabled: Boolean,
    val message: String?
)

class ScreenViewModel(private val usecase: TestUseCase): ViewModel() {

    private val disposable = CompositeDisposable()

    private val mutableLiveData = MutableLiveData<ScreenState>()

    val liveData: LiveData<ScreenState> = mutableLiveData

    fun clickButton() {
        disposable.add(
            usecase.execute()
                .doOnSubscribe {
                    updateState {
                        copy(isLoading = true, isButtonEnabled = false)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    updateState {
                        copy(isLoading = false, isButtonEnabled = true, message = "success")
                    }
                }
                .onErrorComplete()
                .subscribe {
                    updateState {
                        copy(isLoading = false, isButtonEnabled = true, message = "success")
                    }
                }
        )
    }

    private fun updateState(update: ScreenState.() -> ScreenState) {
        mutableLiveData.value = update(mutableLiveData.value ?: initState())
    }

    private fun initState() = ScreenState(isLoading = false, isButtonEnabled = true, message = null)
}


class TestUseCase {

    fun execute(): Completable = Completable.complete()
}
