package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel) : ViewModel
}