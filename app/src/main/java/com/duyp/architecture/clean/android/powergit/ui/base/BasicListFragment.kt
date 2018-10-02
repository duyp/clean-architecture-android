package com.duyp.architecture.clean.android.powergit.ui.base

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.BasicListViewModel

/**
 * Basic fragment to shows a list on RecyclerView. Please see [BasicListViewModel] for more understanding.
 *
 * Used for a fragment which only show a list of data without any other states and intents except [ListState] and
 * [ListIntent]
 */
abstract class BasicListFragment<EntityType, ListType, A: LoadMoreAdapter, VM: BasicListViewModel<EntityType, ListType>> :
        ListFragment<EntityType, ListType, A, ListIntent, ListState, VM>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withState { onListStateUpdated(this) }
    }
}