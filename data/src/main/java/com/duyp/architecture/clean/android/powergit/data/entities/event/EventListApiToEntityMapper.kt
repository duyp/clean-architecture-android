package com.duyp.architecture.clean.android.powergit.data.entities.event

import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity

class EventListApiToEntityMapper: PageableApiMapper<EventApiData, EventEntity>() {

    private val mEventAPiToEntityMapper = EventApiToEntityMapper()

    override fun mapItemFrom(apiData: List<EventApiData>?): List<EventEntity> {
        return mEventAPiToEntityMapper.mapFrom(apiData)
    }
}