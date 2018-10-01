package com.duyp.architecture.clean.android.powergit.ui.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.withState
import dagger.android.support.DaggerFragment
import io.reactivex.subjects.PublishSubject
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class ViewModelFragment<State, Intent, VM : BaseViewModel<State, Intent>> : DaggerFragment() {

    @Inject
    internal lateinit var mViewModelFactory: ViewModelProvider.Factory

    protected val mIntent : PublishSubject<Intent> = PublishSubject.create()

    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(viewModelClass())
        mViewModel.processIntents(mIntent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    abstract fun getLayoutResource(): Int

    protected fun onIntent(intent: Intent) {
        mIntent.onNext(intent)
    }

    protected fun withState(state: State.() -> Unit) {
        withState(mViewModel, state)
    }

    // getting view model class (normally last position is ViewModel type)
    protected fun viewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType)
        return type.actualTypeArguments[type.actualTypeArguments.lastIndex] as Class<VM>
    }
}
