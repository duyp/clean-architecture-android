package com.duyp.architecture.clean.android.powergit.ui.features.drawer

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DrawerModule {

    @Binds
    @IntoMap
    @ViewModelKey(DrawerViewModel::class)
    abstract fun mainViewModel(drawerViewModel: DrawerViewModel): ViewModel
}