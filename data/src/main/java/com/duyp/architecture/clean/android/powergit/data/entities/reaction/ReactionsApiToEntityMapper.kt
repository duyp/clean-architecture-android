package com.duyp.architecture.clean.android.powergit.data.entities.reaction

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.ReactionsEntity

class ReactionsApiToEntityMapper: Mapper<ReactionsApiData, ReactionsEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    override fun mapFrom(e: ReactionsApiData): ReactionsEntity {
        val entity = ReactionsEntity()
        entity.id = e.id
        entity.url = e.url
        entity.content = e.content
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.total_count = e.total_count
        entity.plusOne = e.plusOne
        entity.minusOne = e.minusOne
        entity.laugh = e.laugh
        entity.hooray = e.hooray
        entity.confused = e.confused
        entity.heart = e.heart
        entity.viewerHasReacted = e.viewerHasReacted
        entity.isCallingApi = e.isCallingApi
        return entity
    }
}