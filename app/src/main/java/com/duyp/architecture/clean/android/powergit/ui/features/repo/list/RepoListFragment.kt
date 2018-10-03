package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListFragment
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import javax.inject.Inject

class RepoListFragment: BasicListFragment<RepoEntity, RepoEntity, RepoListAdapter, RepoListViewModel>() {

    @Inject lateinit var mAvatarLoader: AvatarLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.mUsername = arguments?.getString(BundleConstants.EXTRA_USERNAME)
    }

    override fun createAdapter(data: AdapterData<RepoEntity>): RepoListAdapter {
        return RepoListAdapter(data, mAvatarLoader)
    }

}