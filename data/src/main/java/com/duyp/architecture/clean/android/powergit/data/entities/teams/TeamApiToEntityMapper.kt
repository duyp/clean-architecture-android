package com.duyp.architecture.clean.android.powergit.data.entities.teams

import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.TeamsEntity

class TeamApiToEntityMapper: Mapper<TeamApiData, TeamsEntity>() {

    override fun mapFrom(e: TeamApiData): TeamsEntity {
        val entity = TeamsEntity()
        entity.id = e.id
        entity.url = e.url
        entity.name = e.name
        entity.slug = e.slug
        entity.description = e.description
        entity.privacy = e.privacy
        entity.permission = e.permission
        entity.membersUrl = e.membersUrl
        entity.repositoriesUrl = e.repositoriesUrl
        return entity
    }
}