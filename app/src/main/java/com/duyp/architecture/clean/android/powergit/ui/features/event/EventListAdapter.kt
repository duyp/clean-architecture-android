package com.duyp.architecture.clean.android.powergit.ui.features.event

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.LoadMoreAdapter
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader

class EventListAdapter(
        private val noImage: Boolean,
        private val avatarLoader: AvatarLoader,
        private val data: AdapterData<EventEntity>
): LoadMoreAdapter() {

    override fun getTotalItem(): Int {
        return data.getTotalCount()
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder.instance(parent, avatarLoader, noImage)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data.getItemAtPosition(position)?.let { (holder as EventViewHolder).bind(it) }
    }
}