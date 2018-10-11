package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.inflate
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseAdapter
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoViewHolder
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader

class SearchRepoAdapter(
        adapterData: AdapterData<SearchItem>,
        private val mAvatarLoader: AvatarLoader
): BaseAdapter<SearchItem>(adapterData) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                LocalHeaderViewHolder(parent.inflate(R.layout.item_section_header))
            SearchItem.TYPE_SECTION_SEARCH_RESULT ->
                ResultSectionHeader(parent.inflate(R.layout.item_section_header))
            else -> RepoViewHolder.instanceWithAvatar(parent, mAvatarLoader)
        }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                (holder as LocalHeaderViewHolder).mTvTitle.text = "Recent repos"
            SearchItem.TYPE_SECTION_SEARCH_RESULT -> {
                val item = mData.getItemAtPosition(position) as SearchItem.ResultHeader
                holder as ResultSectionHeader
                if (item.errorMessage != null) {
                    holder.mTvTitle.text = item.errorMessage
                } else if (item.loading) {
                    holder.mTvTitle.text = "Searching public repos for \"${item.currentSearchTerm}\"..."
                } else {
                    holder.mTvTitle.text = "Public repos match \"${item.currentSearchTerm}\"" +
                            " (${item.pageCount} pages, ${item.loadedCount} shown)"
                }
            }
            SearchItem.TYPE_ITEM_RECENT -> {
                mData.getItemAtPosition(position)?.let {
                    (it as SearchItem.RecentRepo).getRepo()?.let { repo ->
                        (holder as RepoViewHolder).bindData(repo)
                    }
                }
            }
            SearchItem.TYPE_ITEM_SEARCH_RESULT -> {
                mData.getItemAtPosition(position)?.let {
                    (holder as RepoViewHolder).bindData((it as SearchItem.SearchResultRepo).repo)
                }
            }
        }
    }

    class LocalHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val mTvTitle = itemView.findViewById<TextView>(R.id.tvTitle)!!
    }

    class ResultSectionHeader(itemView: View): RecyclerView.ViewHolder(itemView) {

        val mTvTitle = itemView.findViewById<TextView>(R.id.tvTitle)!!
    }
}