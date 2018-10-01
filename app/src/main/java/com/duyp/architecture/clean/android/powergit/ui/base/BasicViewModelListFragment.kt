package com.duyp.architecture.clean.android.powergit.ui.base

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.BasicListViewModel

abstract class BasicViewModelListFragment<T, A: LoadMoreAdapter, VM: BasicListViewModel<T>> :
        ViewModelListFragment<T, A, ListIntent, ListState, VM>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withState { onListStateUpdated(this) }
    }
}