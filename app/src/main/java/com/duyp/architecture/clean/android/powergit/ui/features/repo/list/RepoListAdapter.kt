package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.duyp.androidutils.glide.loader.GlideLoader
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.LoadMoreAdapter

class RepoListAdapter(
        private val data: AdapterData<RepoEntity>,
        private val glideLoader: GlideLoader,
        private val withAvatar: Boolean = true
): LoadMoreAdapter() {

    override fun getTotalItem(): Int {
        return data.getTotalCount()
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (withAvatar)
            RepoViewHolder.instanceWithAvatar(parent, glideLoader)
        else RepoViewHolder.instanceNoAvatar(parent, glideLoader)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data.getItemAtPosition(position)?.let { (holder as RepoViewHolder).bind(it) }
    }
}