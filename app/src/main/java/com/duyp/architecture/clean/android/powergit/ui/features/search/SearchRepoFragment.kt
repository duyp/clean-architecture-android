package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.addSimpleTextChangedListener
import com.duyp.architecture.clean.android.powergit.ui.base.ListFragment
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import kotlinx.android.synthetic.main.fragment_search_repo.*
import javax.inject.Inject

class SearchRepoFragment: ListFragment<SearchItem, SearchItem, SearchRepoAdapter, SearchRepoIntent, ListState,
        SearchViewModel>() {

    @Inject lateinit var mAvatarLoader: AvatarLoader

    override fun createAdapter(data: AdapterData<SearchItem>): SearchRepoAdapter {
        return SearchRepoAdapter(data, mAvatarLoader)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtSearch.addSimpleTextChangedListener {
            onIntent(SearchRepoIntent.Search(it))
        }

        withState {
            onListStateUpdated(this)
        }
    }

    override fun getLayoutResource() = R.layout.fragment_search_repo
}