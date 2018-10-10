package com.duyp.architecture.clean.android.powergit.ui.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseAdapter
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.LoadMoreAdapter
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginActivity
import com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll.InfiniteScroller
import com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import kotlinx.android.synthetic.main.refresh_recycler_view.*

/**
 * Fragment shows a collection of data in [RecyclerView] with a [LoadMoreAdapter] and
 * [ListViewModel]
 *
 * This fragment contains a [RecyclerView] to show data, a [SwipeRefreshLayout] allowing user to pull down
 * to refresh, a [InfiniteScroller] allowing user to scroll down to load next page, a [RecyclerViewFastScroller]
 * allowing user to fast-scroll with a vertical scroller
 *
 * Please see [ListViewModel] to understand how data is stored and retrieved to be displayed in adapter, how intent
 * is sent to view model and how the view model manage view state of a list
 *
 * @param EntityType type of entity which will be shown in recycler view, is adapter data
 * @param ListType type of data in the [com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity]
 * @param A adapter
 * @param I intent
 * @param S state
 * @param VM view model
 */
abstract class ListFragment<
        EntityType,
        ListType,
        A: BaseAdapter<EntityType>,
        I: ListIntent,
        S,
        VM : ListViewModel<S, I, EntityType, ListType>>
    : ViewModelFragment<S, I, VM>() {

    private lateinit var mAdapter: A

    private lateinit var mInfiniteScroller: InfiniteScroller

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = createAdapter(mViewModel)
        mInfiniteScroller = InfiniteScroller(mAdapter) { onIntent(mViewModel.getLoadMoreIntent()) }

        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(mInfiniteScroller)
        fastScroller.attachRecyclerView(recyclerView)
        refreshLayout.setOnRefreshListener { refresh() }
    }

    override fun getLayoutResource() = R.layout.refresh_recycler_view

    protected abstract fun createAdapter(data: AdapterData<EntityType>): A

    /**
     * Call this to update [ListState] for this fragment, normally called inside [withState]
     */
    protected fun onListStateUpdated(s: ListState) {

        refreshLayout.isEnabled = s.refreshable

        setUiRefreshing(s.showLoading)

        updateEmptyView(s.showEmptyView)

        event(s.refresh) { refresh() }

        event(s.loadCompleted) { onLoadCompleted() }

        event(s.dataUpdated) { onDataUpdated(this) }

        event(s.loadingMore) { onLoadingMore() }

        event(s.errorMessage) { showErrorMessage(this) }

        updateOfflineNotice(s.showOfflineNotice)

        if (s.requireLogin) {
            stateLayout.setEmptyText("Please login")
            stateLayout.setReloadText("Login")
            stateLayout.setOnReloadListener {
                LoginActivity.start(requireContext())
            }
        } else {
            stateLayout.setEmptyText("No Data")
            stateLayout.setReloadText("Reload")
            stateLayout.setOnReloadListener { refresh() }
        }
    }

    protected fun showErrorMessage(message: String) {
        showToastMessage(message)
    }

    protected fun onLoadingMore() {
        mInfiniteScroller.setLoading()
    }

    private fun refresh() {
        onIntent(mViewModel.getRefreshIntent())
        mInfiniteScroller.reset()
    }

    private fun onDataUpdated(diffResult: DiffUtil.DiffResult) {
        mAdapter.update(diffResult)
    }

    private fun onLoadCompleted() {
        mInfiniteScroller.reset()
    }

    private fun setUiRefreshing(refreshing: Boolean) {
        refreshLayout?.post {
            refreshLayout?.isRefreshing = refreshing
        }
    }

    protected fun updateOfflineNotice(showIt: Boolean) {
        tvOfflineNotice.visibility = if (showIt) View.VISIBLE else View.GONE
    }

    protected fun updateEmptyView(showIt: Boolean) {
        if (recyclerView == null || stateLayout == null)
            return

        if (showIt) {
            recyclerView.visibility = View.GONE
            stateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            stateLayout.visibility = View.GONE
        }
    }
}