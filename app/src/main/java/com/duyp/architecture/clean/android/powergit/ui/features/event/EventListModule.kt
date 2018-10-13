package com.duyp.architecture.clean.android.powergit.ui.features.event

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import com.duyp.architecture.clean.android.powergit.di.scopes.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class EventModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun eventViewModel(eventViewModel: EventViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeEventListFragment(): EventListFragment
}