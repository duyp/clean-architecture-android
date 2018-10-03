package com.duyp.architecture.clean.android.powergit.data.entities.milestone

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.MilestoneEntity

class MilestoneApiToEntityMapper: Mapper<MilestoneApiData, MilestoneEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    override fun mapFrom(e: MilestoneApiData): MilestoneEntity {
        val entity = MilestoneEntity()
        entity.id = e.id
        entity.url = e.url
        entity.title = e.title
        entity.state = e.state
        entity.description = e.description
        entity.htmlUr = e.htmlUr
        entity.number = e.number
        entity.openIssues = e.openIssues
        entity.closedIssues = e.closedIssues
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.closedAt = e.closedAt
        entity.dueOn = e.dueOn
        entity.creator = e.creator?.let { mUserApiToEntityMapper.mapFrom(it) }
        return entity
    }
}