package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.arch.lifecycle.ViewModel
import com.duyp.architecture.clean.android.powergit.di.modules.ViewModelKey
import com.duyp.architecture.clean.android.powergit.di.scopes.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class IssueModule {

    @Binds
    @IntoMap
    @ViewModelKey(IssueListViewModel::class)
    abstract fun issueListViewModel(viewModel: IssueListViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeIssueListFragment(): IssueListFragment
}