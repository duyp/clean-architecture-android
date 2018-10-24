package com.duyp.architecture.clean.android.powergit.ui.features.event

import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.getEnum
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListFragment
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import javax.inject.Inject

class EventListFragment: BasicListFragment<EventEntity, EventListAdapter, EventViewModel>() {

    @Inject lateinit var avatarLoader: AvatarLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.username = arguments?.getString(BundleConstants.EXTRA_USERNAME)
        mViewModel.type = arguments?.getEnum<EventType>(BundleConstants.EXTRA_TYPE)
    }

    override fun createAdapter(data: AdapterData<EventEntity>): EventListAdapter {
        return EventListAdapter(mViewModel.type == EventType.SELF, avatarLoader, data)
    }
}