package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class IssueLocalToEntityMapper: Mapper<IssueLocalData, IssueEntity>() {

    private val mUserLocalToEntityMapper = UserLocalToEntityMapper()

    override fun mapFrom(e: IssueLocalData): IssueEntity {
        val entity = IssueEntity()
        entity.id = e.id
        entity.url = e.url
        entity.body = e.body
        entity.title = e.title
        entity.comments = e.comments
        entity.number = e.number
        entity.locked = e.locked
        entity.state = e.state
        entity.repoUrl = e.repoUrl
        entity.bodyHtml = e.bodyHtml
        entity.htmlUrl = e.htmlUrl
        entity.closedAt = e.closedAt
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.repoName = e.repoName
        entity.repoOwner = e.repoOwner
        entity.user = e.user?.let { mUserLocalToEntityMapper.mapFrom(it) }
//        entity.assignee = e.assignee
//        entity.closedBy = e.closedBy
//        entity.assignees = e.assignees
//        entity.labels = e.labels
//        entity.milestone = e.milestone
//        entity.repository = e.repository
//        entity.pullRequest = e.pullRequest
//        entity.reactions = e.reactions
        return entity

    }
}