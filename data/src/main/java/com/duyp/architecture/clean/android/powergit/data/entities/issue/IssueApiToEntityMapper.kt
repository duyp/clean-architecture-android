package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.milestone.MilestoneApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.pullrequest.PullRequestApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class IssueApiToEntityMapper: Mapper<IssueApiData, IssueEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mLabelApiToEntityMapper = LabelApiToEntityMapper()

    private val mMileStoneApiToEntityMapper = MilestoneApiToEntityMapper()

    private val mPullRequestApiToEntityMapper = PullRequestApiToEntityMapper()

    private val mReactionsApiToEntityMapper = ReactionsApiToEntityMapper()

    override fun mapFrom(e: IssueApiData): IssueEntity {
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
        entity.login = e.login
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.assignee = e.assignee?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.closedBy = e.closedBy?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.assignees = e.assignees?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.labels = e.labels?.let { mLabelApiToEntityMapper.mapFrom(it) }
        entity.milestone = e.milestone?.let { mMileStoneApiToEntityMapper.mapFrom(it) }
        entity.repository = e.repository
        entity.pullRequest = e.pullRequest?.let { mPullRequestApiToEntityMapper.mapFrom(it) }
        entity.reactions = e.reactions?.let { mReactionsApiToEntityMapper.mapFrom(it) }
        return entity
    }
}