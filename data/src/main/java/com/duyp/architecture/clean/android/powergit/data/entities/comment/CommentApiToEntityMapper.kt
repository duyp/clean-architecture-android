package com.duyp.architecture.clean.android.powergit.data.entities.comment

import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.CommentEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class CommentApiToEntityMapper: Mapper<CommentApiData, CommentEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mReactionApiToEntityMapper = ReactionsApiToEntityMapper()

    override fun mapFrom(e: CommentApiData): CommentEntity {
        val entity = CommentEntity()
        entity.id = e.id
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.url = e.url
        entity.body = e.body
        entity.bodyHtml = e.bodyHtml
        entity.htmlUrl = e.htmlUrl
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.path = e.path
        entity.commitId = e.commitId
        entity.repoId = e.repoId
        entity.login = e.login
        entity.gistId = e.gistId
        entity.issueId = e.issueId
        entity.pullRequestId = e.pullRequestId
        entity.authorAssociation = e.authorAssociation
        entity.reactions = e.reactions?.let { mReactionApiToEntityMapper.mapFrom(it) }
        entity.position = e.position
        entity.line = e.line
        return entity
    }
}