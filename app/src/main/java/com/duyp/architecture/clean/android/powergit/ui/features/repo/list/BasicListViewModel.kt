package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.ui.base.ListIntent
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.ui.base.ListViewModel

abstract class BasicListViewModel<T>: ListViewModel<ListState, ListIntent, T>() {

    override fun initState() = ListState()

    override fun setListState(s: ListState.() -> ListState) {
        setState(s)
    }

    override fun withListState(s: ListState.() -> Unit) {
        withState(s)
    }

    override fun getRefreshIntent() = ListIntent.RefreshIntent

    override  fun getLoadMoreIntent() = ListIntent.LoadMoreIntent
}