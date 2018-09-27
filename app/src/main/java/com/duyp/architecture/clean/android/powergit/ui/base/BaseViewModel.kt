package com.duyp.architecture.clean.android.powergit.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.postValueIfNew
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Base class of any ViewModel containing boilerplate codes to process View State, Intent and handle disposables.
 * The ViewModel receives view interaction via Intent and populates view state to update the view
 * [S] type of view state
 * [I] type of intent
 */
abstract class BaseViewModel<S, I> : ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()

    // intent
    private val mIntentSubject = PublishSubject.create<I>()

    val state: MutableLiveData<S> = MutableLiveData()

    /**
     * Process intents by subscribing to [mIntentSubject]
     * @param intents intents
     */
    fun processIntents(intents: Observable<I>) {
        composeIntent(mIntentSubject)
        intents.subscribe(mIntentSubject)
    }

    /**
     * Compose all intents based on given intents subject
     * @param intentSubject the subject which will be subscribed by view's intents, see [processIntents]
     */
    protected abstract fun composeIntent(intentSubject: Observable<I>)

    /**
     * Provide initial state to be used in [setState] in which the new state will be produced from current state,
     * therefore current state should not be null
     */
    abstract fun initState() : S

    /**
     * set new view state and post it through [state] live data
     *
     * @param stateFunc provide new state by applying current state
     */
    protected fun setState(stateFunc: S.() -> S) {
        state.postValueIfNew(stateFunc.invoke(state.value ?: initState()))
    }

    /**
     * Add an Rx's disposable into [mCompositeDisposable]. All disposables will be disposed when the ViewModel is
     * being cleared, see [onCleared]
     */
    protected fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }


}
