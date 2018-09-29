package com.duyp.architecture.clean.android.powergit.ui.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.postValueIfNew
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Base class of any [ViewModel] containing boilerplate codes to process View State, Intent and handle disposables.
 *
 * The ViewModel receives view interactions via Intent [I] and populates view state [S] to update the view.
 *
 * When activities / fragments are created, they must call [processIntents] to subscribe their intents into the view
 * model's [mIntentSubject] then the view model can process ongoing events sent from the view.
 *
 * The [state] is a [MutableLiveData] which any instance of [LifecycleOwner] can observe to get updated data
 *
 * To populate new state, the view model should call [setState] in which new state is produced based on the current
 * state.
 *
 * All Rx executions should be added to [mCompositeDisposable] (via [addDisposable]) to be disposed when the view model
 * is being cleared. See [onCleared]
 *
 * [S] type of view state
 * [I] type of intent
 */
abstract class BaseViewModel<S, I> : ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()

    // intent
    private val mIntentSubject = PublishSubject.create<I>()

    // view state
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
     * Provide initial state to be used in [setState] in which the new state will be produced from current state,
     * therefore current state should not be null
     */
    protected abstract fun initState() : S

    /**
     * Compose all intents based on given intents subject
     * @param intentSubject the subject which will be subscribed by view's intents, see [processIntents]
     */
    protected abstract fun composeIntent(intentSubject: Observable<I>)

    /**
     * access current state, if current state is null, [initState] will be passed
     */
    protected fun withState(stateConsumer: S.() -> Unit) {
        stateConsumer.invoke(this.state.value ?: initState())
    }

    /**
     * Get current state, [initState] will be returned if current state is null
     */
    protected fun state() = state.value ?: initState()

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
    protected fun addDisposable(disposable: () -> Disposable) {
        mCompositeDisposable.add(disposable.invoke())
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }


}
