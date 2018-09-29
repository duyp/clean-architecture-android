package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ActivityModule
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel

}

@Module
class MainActivityModule : ActivityModule<MainActivity>()