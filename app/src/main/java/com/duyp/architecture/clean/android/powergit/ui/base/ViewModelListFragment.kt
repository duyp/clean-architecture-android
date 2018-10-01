package com.duyp.architecture.clean.android.powergit.ui.base

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll.InfiniteScroller
import kotlinx.android.synthetic.main.refresh_recycler_view.*

abstract class ViewModelListFragment<T, A: LoadMoreAdapter, I: ListIntent, S, VM: ListViewModel<S, I, T>>:
        ViewModelFragment<S, I, VM>() {

    private lateinit var mAdapter: A

    private lateinit var mInfiniteScroller: InfiniteScroller

    private var mIsRefreshing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = createAdapter(mViewModel)
        mInfiniteScroller = InfiniteScroller(mAdapter) { onIntent(mViewModel.getLoadMoreIntent()) }

        recyclerView.adapter = mAdapter
        recyclerView.setEmptyView(stateLayout, refreshLayout)
        recyclerView.addOnScrollListener(mInfiniteScroller)
        fastScroller.attachRecyclerView(recyclerView)
        refreshLayout.setOnRefreshListener { refresh() }
    }

    override fun getLayoutResource() = R.layout.refresh_recycler_view

    abstract protected fun createAdapter(data: AdapterData<T>): A
    
    protected fun onListStateUpdated(s: ListState) {
        setUiRefreshing(s.showLoading)

        event(s.refresh) { refresh() }

        event(s.loadCompleted) { doneRefresh() }

        event(s.loadingMore) { onLoadingMore() }

        event(s.errorMessage) { showErrorMessage(this) }

        if (s.showOfflineNotice) {
            showOfflineNotice()
        }
    }

    protected fun showOfflineNotice() {
        showToastMessage("Showing offline data")
    }

    protected fun showErrorMessage(message: String) {
        showToastMessage(message)
    }

    protected fun onLoadingMore() {
        mInfiniteScroller.setLoading()
    }

    private fun refresh() {
        if (!mIsRefreshing) {
            onIntent(mViewModel.getRefreshIntent())
            mInfiniteScroller.reset()
            mIsRefreshing = true
        }
    }

    private fun doneRefresh() {
        mInfiniteScroller.reset()
        mAdapter.notifyDataSetChanged()
        mIsRefreshing = false
    }

    private fun setUiRefreshing(refreshing: Boolean) {
        refreshLayout.post {
            refreshLayout.isRefreshing = refreshing
        }
    }
}