package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListFragment
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import javax.inject.Inject

class RepoListFragment: BasicListFragment<RepoEntity, RepoEntity, RepoListAdapter, RepoListViewModel>() {

    @Inject lateinit var mAvatarLoader: AvatarLoader

    override fun createAdapter(data: AdapterData<RepoEntity>): RepoListAdapter {
        return RepoListAdapter(data, mAvatarLoader)
    }

}