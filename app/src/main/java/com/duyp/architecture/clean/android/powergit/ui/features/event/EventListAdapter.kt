package com.duyp.architecture.clean.android.powergit.ui.features.event

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseAdapter
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader

class EventListAdapter(
        private val noImage: Boolean,
        private val avatarLoader: AvatarLoader,
        data: AdapterData<EventEntity>
): BaseAdapter<EventEntity>(data) {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder.instance(parent, avatarLoader, noImage)
    }
}