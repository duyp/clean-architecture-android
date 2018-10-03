package com.duyp.architecture.clean.android.powergit.data.entities.event

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class EventApiToEntityMapper: Mapper<EventApiData, EventEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mPayloadApiToEntityMapper = PayloadApiToEntityMapper()

    override fun mapFrom(e: EventApiData): EventEntity {
        val entity = EventEntity()
        entity.id = e.id
        entity.type = e.type
        entity.createdAt = e.createdAt
        entity.actor = e.actor?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.repo = e.repo
        entity.payload = e.payload?.let { mPayloadApiToEntityMapper.mapFrom(it) }
        entity.publicEvent = e.publicEvent
        return entity
    }
}