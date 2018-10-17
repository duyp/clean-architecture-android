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
        private val mRecentTabClickListener: (Int) -> Unit,
        private val mReloadResultAction: () -> Unit
): BaseAdapter<SearchItem>(adapterData) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SearchItem.TYPE_SECTION_RECENT ->
                LocalHeaderViewHolder(parent.inflate(R.layout.item_search_recent_header), mRecentTabClickListener)
            SearchItem.TYPE_SECTION_SEARCH_RESULT ->
                ResultSectionHeader(parent.inflate(R.layout.item_search_result_header), mReloadResultAction)
            SearchItem.TYPE_ITEM_RECENT_ISSUE -> IssueViewHolder.newInstance(parent, mAvatarLoader, true, true, true)
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
            SearchItem.TYPE_ITEM_SEARCH_RESULT -> {
                mData.getItemAtPosition(position)?.let {
                    (holder as RepoViewHolder).bindData((it as SearchItem.SearchResultRepo).repo)
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
            private val mRecentTabClickListener: (Int) -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        private val mTvNoData = itemView.findViewById<TextView>(R.id.tvNoData)!!

        private val tvs: List<TextView> = listOf(
                itemView.findViewById(R.id.tvRepo)!!,
                itemView.findViewById(R.id.tvIssue)!!,
                itemView.findViewById(R.id.tvUser)!!
        )

        init {
            tvs.forEachIndexed { index, textView ->
                textView.setOnClickListener { mRecentTabClickListener(index) }
            }
        }

        internal fun bind(oldItem: SearchItem.RecentHeader?, newItem: SearchItem.RecentHeader) {
            val newTab = newItem.currentTab
            val tabChanged = oldItem != null && oldItem.currentTab != newTab
            if (tabChanged) {
                val oldTab = oldItem?.currentTab ?: 0
                tvs[oldTab].text = getCountText(oldTab, newItem)
                tvs[newTab].startExpandingAnimation(getTitle(newTab), 150)
            } else {
                // invalidate tabs
                tvs.forEachIndexed { index, textView -> textView.text = getCountText(index, newItem) }
                tvs[newTab].text = getTitle(newTab)
            }

            // invalidate no data text
            when (newTab) {
                0 -> {
                    mTvNoData.visibility = if (newItem.repoCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent repos", newItem.currentSearchTerm)
                }
                1 -> {
                    mTvNoData.visibility = if (newItem.issueCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent issues", newItem.currentSearchTerm)
                }
                2 -> {
                    mTvNoData.visibility = if (newItem.userCount > 0) View.GONE else View.VISIBLE
                    setNoDataText("No recent users", newItem.currentSearchTerm)
                }
            }
        }

        private fun getCountText(tab: Int, item: SearchItem.RecentHeader): String {
            val count = when (tab) {
                0 -> item.repoCount
                1 -> item.issueCount
                2 -> item.userCount
                else -> 0
            }
            return if (count > 0) "($count)" else ""
        }

        private fun getTitle(tab: Int) = when(tab) {
            0 -> "Recent repos"
            1 -> "Recent issues"
            2 -> "Recent users"
            else -> ""
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
                mTvTitle.text = "Public repos match \"${item.currentSearchTerm}\"\n" +
                        "(${item.pageCount} pages, ${item.loadedCount} shown)"
            } else {
                mTvTitle.setVisible(false)
            }
        }
    }
}