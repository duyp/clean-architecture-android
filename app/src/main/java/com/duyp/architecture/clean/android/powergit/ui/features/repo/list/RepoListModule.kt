package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.FragmentModule
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import com.duyp.architecture.clean.android.powergit.di.scopes.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RepoListModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepoListViewModel::class)
    abstract fun repoListViewModel(repoListViewModel: RepoListViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector(modules = [RepoListFragmentModule::class])
    abstract fun contributeRepoListFragment(): RepoListFragment
}

@Module
internal class RepoListFragmentModule: FragmentModule<RepoListFragment>()