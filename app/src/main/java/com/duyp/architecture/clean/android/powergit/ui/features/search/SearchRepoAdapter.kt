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
        private val mAvatarLoader: AvatarLoader,
        private val mRecentTabClickListener: (Int) -> Unit
): BaseAdapter<SearchItem>(adapterData) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                LocalHeaderViewHolder(parent.inflate(R.layout.item_search_header), mRecentTabClickListener)
            SearchItem.TYPE_SECTION_SEARCH_RESULT ->
                ResultSectionHeader(parent.inflate(R.layout.item_section_header))
            else -> RepoViewHolder.instanceWithAvatar(parent, mAvatarLoader)
        }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                (holder as LocalHeaderViewHolder).bind(mData.getItemAtPosition(position) as SearchItem.RecentHeader)
            SearchItem.TYPE_SECTION_SEARCH_RESULT -> {
                (holder as ResultSectionHeader).bind(mData.getItemAtPosition(position) as SearchItem.ResultHeader)
            }
            SearchItem.TYPE_ITEM_RECENT_REPO -> {
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

    class LocalHeaderViewHolder(
            itemView: View,
            private val mRecentTabClickListener: (Int) -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        private val mTvRepo = itemView.findViewById<TextView>(R.id.tvRepo)!!
        private val mTvIssue = itemView.findViewById<TextView>(R.id.tvIssue)!!
        private val mTvUser = itemView.findViewById<TextView>(R.id.tvUser)!!

        private val mColorAccent = itemView.context.resources.getColor(R.color.colorAccent)
        private val mColorGrey = itemView.context.resources.getColor(R.color.dark_grey)

        init {
            mTvRepo.setOnClickListener { mRecentTabClickListener.invoke(0) }
            mTvIssue.setOnClickListener { mRecentTabClickListener.invoke(1) }
            mTvUser.setOnClickListener { mRecentTabClickListener.invoke(2) }
        }

        internal fun bind(item: SearchItem.RecentHeader) {
            setCount(mTvRepo, item.repoCount)
            setCount(mTvIssue, item.issueCount)
            setCount(mTvUser, item.userCount)
            when(item.currentTab) {
                0 -> {
                    mTvRepo.text = "Recent repos"
                    mTvRepo.setTextColor(mColorAccent)
                }
                1 -> {
                    mTvIssue.text = "Recent issues"
                    mTvIssue.setTextColor(mColorAccent)
                }
                2 -> {
                    mTvUser.text = "Recent users"
                    mTvUser.setTextColor(mColorAccent)
                }
            }
        }

        private fun setCount(textView: TextView, count: Int) {
            textView.text = if (count > 0) "($count)" else ""
            textView.isEnabled = count > 0
            textView.setTextColor(mColorGrey)
        }
    }

    class ResultSectionHeader(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mTvTitle = itemView.findViewById<TextView>(R.id.tvTitle)!!

        internal fun bind(item: SearchItem.ResultHeader) {
            if (item.errorMessage != null) {
                mTvTitle.text = item.errorMessage
            } else if (item.loading) {
                mTvTitle.text = "Searching public repos for \"${item.currentSearchTerm}\"..."
            } else {
                mTvTitle.text = "Public repos match \"${item.currentSearchTerm}\"" +
                        " (${item.pageCount} pages, ${item.loadedCount} shown)"
            }
        }
    }
}