package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.ui.base.LoadMoreAdapter

class RepoListAdapter(
        context: Context,
        private val vm: RepoListViewModel
): LoadMoreAdapter(context) {

    override fun getTotalItem(): Int {
        return vm.totalItemCount()
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepoViewHolder(mLayoutInflater.inflate(R.layout.repos_row_item, parent, false))
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        vm.getItemAtPosition(position)?.let { (holder as RepoViewHolder).bind(it) }
    }
}