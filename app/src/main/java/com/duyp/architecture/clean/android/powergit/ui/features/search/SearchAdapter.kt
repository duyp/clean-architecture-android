package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.duyp.architecture.clean.android.powergit.*
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseAdapter
import com.duyp.architecture.clean.android.powergit.ui.features.issue.list.IssueViewHolder
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoViewHolder
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader

class SearchAdapter(
        adapterData: AdapterData<SearchItem>,
        private val mAvatarLoader: AvatarLoader,
        private val mRecentTabClickListener: (SearchTab) -> Unit,
        private val mReloadResultAction: () -> Unit
): BaseAdapter<SearchItem>(adapterData) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                LocalHeaderViewHolder(parent.inflate(R.layout.item_search_recent_header), mRecentTabClickListener)
            SearchItem.TYPE_SECTION_SEARCH_RESULT ->
                ResultSectionHeader(parent.inflate(R.layout.item_search_result_header), mReloadResultAction)
            SearchItem.TYPE_ITEM_RECENT_ISSUE, SearchItem.TYPE_ITEM_SEARCH_RESULT_ISSUE ->
                IssueViewHolder.newInstance(parent, mAvatarLoader, true, true, true)
            else -> RepoViewHolder.instanceWithAvatar(parent, mAvatarLoader)
        }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                (holder as LocalHeaderViewHolder).bind(null,
                        mData.getItemAtPosition(position) as SearchItem.RecentHeader)
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
            SearchItem.TYPE_ITEM_RECENT_ISSUE -> {
                mData.getItemAtPosition(position)?.let {
                    (it as SearchItem.RecentIssue).getIssue()?.let { issue ->
                        (holder as IssueViewHolder).bindData(issue)
                    }
                }
            }
            SearchItem.TYPE_ITEM_SEARCH_RESULT_REPO -> {
                mData.getItemAtPosition(position)?.let {
                    (holder as RepoViewHolder).bindData((it as SearchItem.SearchResultRepo).repo)
                }
            }
            SearchItem.TYPE_ITEM_SEARCH_RESULT_ISSUE -> {
                mData.getItemAtPosition(position)?.let {
                    (holder as IssueViewHolder).bindData((it as SearchItem.SearchResultIssue).issue)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        if (holder is LocalHeaderViewHolder) {
            val oldItem = payloads[0] as SearchItem.RecentHeader
            val newItem = mData.getItemAtPosition(position) as SearchItem.RecentHeader
            holder.bind(oldItem, newItem)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    class LocalHeaderViewHolder(
            itemView: View,
            private val mRecentTabClickListener: (SearchTab) -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        private val mTvNoData = itemView.findViewById<TextView>(R.id.tvNoData)!!

        private val tvs: List<TextView> = listOf(
                itemView.findViewById(R.id.tvRepo)!!,
                itemView.findViewById(R.id.tvIssue)!!,
                itemView.findViewById(R.id.tvUser)!!
        )

        init {
            tvs.forEachIndexed { index, textView ->
                textView.setOnClickListener { mRecentTabClickListener(SearchTab.of(index)) }
            }
        }

        internal fun bind(oldItem: SearchItem.RecentHeader?, newItem: SearchItem.RecentHeader) {
            val newTab = newItem.currentTab
            val tabChanged = oldItem != null && oldItem.currentTab != newTab
            if (tabChanged) {
                val oldTab = oldItem!!.currentTab
                tvs[oldTab.position].text = getCountText(oldTab, newItem)
                tvs[newTab.position].startExpandingAnimation(getTitle(newTab), 150)
            } else {
                // invalidate tabs
                tvs.forEachIndexed { index, textView -> textView.text = getCountText(SearchTab.of(index), newItem) }
                tvs[newTab.position].text = getTitle(newTab)
            }

            // invalidate no data text
            when (newTab) {
                SearchTab.REPO -> {
                    mTvNoData.visibility = if (newItem.repoCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent repos", newItem.currentSearchTerm)
                }
                SearchTab.ISSUE -> {
                    mTvNoData.visibility = if (newItem.issueCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent issues", newItem.currentSearchTerm)
                }
                SearchTab.USER -> {
                    mTvNoData.visibility = if (newItem.userCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent users", newItem.currentSearchTerm)
                }
            }
        }

        private fun getCountText(tab: SearchTab, item: SearchItem.RecentHeader): String {
            val count = when (tab) {
                SearchTab.REPO -> item.repoCount
                SearchTab.ISSUE -> item.issueCount
                SearchTab.USER -> item.userCount
            }
            return if (count > 0) "($count)" else ""
        }

        private fun getTitle(tab: SearchTab) = when(tab) {
            SearchTab.REPO -> "Recent repos"
            SearchTab.ISSUE -> "Recent issues"
            SearchTab.USER -> "Recent users"
        }

        private fun setNoDataText(prefix: String, searchTerm: String) {
            mTvNoData.text = prefix + " match \"$searchTerm\""
        }

    }

    class ResultSectionHeader(
            itemView: View,
            private val mReloadAction: () -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        private val mTvTitle = itemView.findViewById<TextView>(R.id.tvTitle)!!
        private val mTvError = itemView.findViewById<TextView>(R.id.tvError)!!
        private val mProgressBar = itemView.findViewById<ProgressBar>(R.id.progress)!!

        init {
            mTvError.setOnClickListener { mReloadAction() }
        }

        internal fun bind(item: SearchItem.ResultHeader) {
            mProgressBar.setVisible(item.loading)
            mTvError.setVisible(item.errorMessage != null)
            if (item.errorMessage != null) {
                mTvError.text = item.errorMessage
            }
            if (!item.loading && item.errorMessage.nullOrEmpty()) {
                mTvTitle.setVisible(true)
                mTvTitle.text = "Public repos match \"${item.currentSearchTerm}\" (${item.totalCount})"
            } else {
                mTvTitle.setVisible(false)
            }
        }
    }
}