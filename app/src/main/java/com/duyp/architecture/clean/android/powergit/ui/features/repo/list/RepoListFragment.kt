package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelFragment
import com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll.InfiniteScroller
import kotlinx.android.synthetic.main.refresh_recycler_view.*

class RepoListFragment: ViewModelFragment<RepoListState, RepoListIntent, RepoListViewModel>() {

    private lateinit var mAdapter: RepoListAdapter

    private lateinit var mInfiniteScroller: InfiniteScroller

    private var mIsRefreshing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = RepoListAdapter(requireContext(), mViewModel)
        mInfiniteScroller = InfiniteScroller(mAdapter) { onIntent(RepoListIntent.LoadMoreIntent) }

        recyclerView.adapter = mAdapter
        recyclerView.setEmptyView(stateLayout, refreshLayout)
        recyclerView.addOnScrollListener(mInfiniteScroller)

        refreshLayout.setOnRefreshListener { refresh() }

        withState {
            setRefreshing(isLoading)

            event(refresh) { refresh() }

            event(onLoadCompleted) { doneRefresh() }

            event(onLoadingMore) { mInfiniteScroller.setLoading() }

            event(errorMessage) { showToastMessage(this) }

            if (showOfflineNotice) {
                // Todo show offline notice
                showToastMessage("Showing offline data")
            }
        }
    }

    override fun getLayoutResource() = R.layout.refresh_recycler_view

    fun refresh() {
        if (!mIsRefreshing) {
            onIntent(RepoListIntent.RefreshIntent)
            mInfiniteScroller.reset()
            setRefreshing(true)
        }
    }

    fun doneRefresh() {
        mInfiniteScroller.reset()
        mAdapter.notifyDataSetChanged()
    }

    fun setRefreshing(refreshing: Boolean) {
        mIsRefreshing = refreshing
        refreshLayout.post {
            refreshLayout.isRefreshing = refreshing
        }
    }
}