package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.ui.base.ListFragment
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import javax.inject.Inject

class IssueListFragment: ListFragment<IssueEntity, IssueEntity, IssueAdapter, IssueListIntent, IssueListState,
        IssueListViewModel>() {

    @Inject internal lateinit var mAvatarLoader: AvatarLoader

    override fun createAdapter(data: AdapterData<IssueEntity>): IssueAdapter {
        return IssueAdapter(data, mAvatarLoader)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withState {
            onListStateUpdated(listState)
        }
    }
}
