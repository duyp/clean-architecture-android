package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelFragment
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview.scroll.InfiniteScroller
import kotlinx.android.synthetic.main.fragment_search_repo.*
import javax.inject.Inject

class SearchFragment: ViewModelFragment<SearchState, SearchIntent, SearchViewModel>() {

    @Inject internal lateinit var mAvatarLoader: AvatarLoader

    private lateinit var mAdapter: SearchAdapter

    private lateinit var mInfiniteScroller: InfiniteScroller

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabListener: (Int) -> Unit = {
            onIntent(SearchIntent.SelectTab(it))
        }
        val reloadResultAction = {
            onIntent(SearchIntent.ReloadResult)
        }
        mAdapter = SearchAdapter(mViewModel, mAvatarLoader, tabListener, reloadResultAction)

        mInfiniteScroller = InfiniteScroller(mAdapter) { onIntent(SearchIntent.LoadMore) }
        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(mInfiniteScroller)
        fastScroller.attachRecyclerView(recyclerView)

        withState {
            event(errorMessage) { showToastMessage(this) }
            event(dataUpdated) { mAdapter.update(this) }
            event(loadingMore) { mInfiniteScroller.setLoading() }
            event(loadCompleted) { mInfiniteScroller.reset() }
        }
    }

    fun onSearch(text: String) {
        onIntent(SearchIntent.Search(text))
    }

    override fun getLayoutResource() = R.layout.fragment_search_repo
}