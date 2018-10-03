package com.duyp.architecture.clean.android.powergit.ui.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.withState
import io.reactivex.subjects.PublishSubject
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

/**
 * Base activity with View Model which is provided by [ViewModelProvider] with injected [ViewModelProvider.Factory]
 *
 * @param [State] type of view state
 * @param [Intent] type of view intent
 * @param [VM] type of view model
 */
abstract class ViewModelActivity<State, Intent, VM : BaseViewModel<State, Intent>> : BaseActivity() {

    @Inject
    internal lateinit var mViewModelFactory: ViewModelProvider.Factory

    private val mIntent : PublishSubject<Intent> = PublishSubject.create()

    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // getting view model class (3rd position is ViewModel type)
        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<VM>

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(viewModelClass)
        mViewModel.processIntents(mIntent)
    }

    protected fun onIntent(intent: Intent) {
        mIntent.onNext(intent)
    }

    protected fun withState(state: State.() -> Unit) {
        withState(mViewModel, state)
    }

    companion object {
        const val NO_LAYOUT = 0
    }
}
