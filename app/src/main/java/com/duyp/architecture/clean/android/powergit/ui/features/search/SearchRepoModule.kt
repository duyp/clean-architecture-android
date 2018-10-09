package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ActivityModule
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import com.duyp.architecture.clean.android.powergit.di.scopes.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchRepoModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun searchViewModel(searchViewModel: SearchViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun searchRepoFragment(): SearchRepoFragment
}

@Module
class SearchRepoActivityModule: ActivityModule<SearchRepoActivity>()