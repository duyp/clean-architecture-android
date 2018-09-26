package com.duyp.architecture.clean.android.powergit.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class ViewModelActivity<B : ViewDataBinding, VM : ViewModel> : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var mViewModelFactory: ViewModelProvider.Factory

    protected lateinit var mViewModel : VM

    protected lateinit var mBinding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init data binding
        mBinding = DataBindingUtil.setContentView(this, getLayoutResource())

        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(viewModelClass)
    }

    abstract fun getLayoutResource() : Int
}
