package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.BasicViewModelListFragment

class RepoListFragment: BasicViewModelListFragment<RepoEntity, RepoListAdapter, RepoListViewModel>() {

    override fun createAdapter(data: AdapterData<RepoEntity>): RepoListAdapter {
        return RepoListAdapter(requireContext(), data)
    }

}