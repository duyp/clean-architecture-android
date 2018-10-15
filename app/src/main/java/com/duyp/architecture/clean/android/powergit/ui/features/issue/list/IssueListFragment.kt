package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.getEnum
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_REPO_NAME
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_TYPE
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_USERNAME
import com.duyp.architecture.clean.android.powergit.ui.base.ListFragment
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import javax.inject.Inject

class IssueListFragment: ListFragment<IssueEntity, IssueEntity, IssueAdapter, IssueListIntent, IssueListState,
        IssueListViewModel>() {

    @Inject internal lateinit var mAvatarLoader: AvatarLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val type = arguments!!.getEnum<IssueListType>(EXTRA_TYPE)
            if (type == IssueListType.REPO_ISSUES) {
                mViewModel.initWithRepo(arguments!![EXTRA_USERNAME].toString(), arguments!![EXTRA_REPO_NAME].toString())
            } else {
                mViewModel.initWithUser(arguments!![EXTRA_USERNAME]?.toString())
            }
        }
    }

    override fun createAdapter(data: AdapterData<IssueEntity>): IssueAdapter {
        return IssueAdapter(
                data = data,
                avatarLoader = mAvatarLoader,
                showRepoName = mViewModel.listType != IssueListType.REPO_ISSUES
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withState {
            onListStateUpdated(listState)
        }
    }
}
