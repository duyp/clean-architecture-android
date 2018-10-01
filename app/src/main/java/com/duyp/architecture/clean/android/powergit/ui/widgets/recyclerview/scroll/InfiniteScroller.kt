package com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

import com.duyp.architecture.clean.android.powergit.ui.base.LoadMoreAdapter

class InfiniteScroller(
        private val mAdapter: LoadMoreAdapter,
        private var mVisibleThreshold: Int = 3,
        private val mOnLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var mLoading = false

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    private var mIsNewlyAdded = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (mIsNewlyAdded) {
            mIsNewlyAdded = false
            return
        }

        if (mLayoutManager == null) {
            initLayoutManager(recyclerView!!.layoutManager)
        }
        val totalItemCount = mLayoutManager!!.itemCount

        if (mLoading || totalItemCount == 0 || mAdapter.isProgressAdded()) return

        var lastVisibleItemPosition = 0
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (lastVisibleItemPosition + mVisibleThreshold > totalItemCount) {
            mOnLoadMore()
        }
    }

    fun reset() {
        mLoading = false
        mAdapter.removeProgress()
    }

    fun setLoading() {
        mLoading = true
        mAdapter.addProgress()
    }

    private fun initLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.mLayoutManager = layoutManager
        if (layoutManager is GridLayoutManager) {
            mVisibleThreshold *= layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            mVisibleThreshold *= layoutManager.spanCount
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}

