package com.duyp.architecture.clean.android.powergit.data.entities.gist

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.GistEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class GistApiToEntityMapper: Mapper<GistApiData, GistEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    override fun mapFrom(e: GistApiData): GistEntity {
        val entity = GistEntity()
        entity.id = e.id
        entity.gistId = e.gistId
        entity.url = e.url
        entity.forksUrl = e.forksUrl
        entity.commitsUrl = e.commitsUrl
        entity.gitPullUrl = e.gitPullUrl
        entity.gitPushUrl = e.gitPushUrl
        entity.htmlUrl = e.htmlUrl
        entity.ownerName = e.ownerName
        entity.description = e.description
        entity.commentsUrl = e.commentsUrl
        entity.publicX = e.publicX
        entity.truncated = e.truncated
        entity.comments = e.comments
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.owner = e.owner?.let { mUserApiToEntityMapper.mapFrom(it) }
        return entity
    }

}