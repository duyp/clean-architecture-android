package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseAdapter
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader

class IssueAdapter(
        data: AdapterData<IssueEntity>,
        private val avatarLoader: AvatarLoader,
        private val withAvatar: Boolean = true,
        private val showRepoName: Boolean = true,
        private val showIssueState: Boolean = true
): BaseAdapter<IssueEntity>(data) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IssueViewHolder.newInstance(parent, avatarLoader, withAvatar, showRepoName, showIssueState)
    }
}