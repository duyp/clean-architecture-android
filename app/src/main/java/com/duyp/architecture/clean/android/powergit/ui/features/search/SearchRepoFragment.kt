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

class SearchRepoFragment: ViewModelFragment<SearchState, SearchRepoIntent, SearchViewModel>() {

    @Inject internal lateinit var mAvatarLoader: AvatarLoader

    private lateinit var mAdapter: SearchRepoAdapter

    private lateinit var mInfiniteScroller: InfiniteScroller

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = SearchRepoAdapter(mViewModel, mAvatarLoader)
        mInfiniteScroller = InfiniteScroller(mAdapter) { onIntent(SearchRepoIntent.LoadMore) }
        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(mInfiniteScroller)
        fastScroller.attachRecyclerView(recyclerView)

        withState {
            event(errorMessage) { showToastMessage(this) }
            event(loadingMore) { mInfiniteScroller.setLoading() }
            event(dataUpdated) { mAdapter.update(this) }
            event(loadCompleted) { mInfiniteScroller.reset() }
        }
    }

    fun onSearch(text: String) {
        onIntent(SearchRepoIntent.Search(text))
    }

    override fun getLayoutResource() = R.layout.fragment_search_repo
}