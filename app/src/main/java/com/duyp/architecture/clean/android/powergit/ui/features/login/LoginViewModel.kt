package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.usecases.LoginUser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val mLoginUser: LoginUser) : ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()

    var isLoading = false

    val errorMessage: MutableLiveData<Event<String>> = MutableLiveData()

    val loginSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.value = Event("Please input username and password")
        } else {
            addDisposable(mLoginUser.login(username, password)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { isLoading = true }
                    .subscribe({
                        loginSuccess.postValue(Event(true))
                    }, { throwable ->
                        isLoading = false
                        throwable.message?.let { errorMessage.postValue(Event(it)) }
                    })
            )
        }
    }

    internal fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}

