package com.duyp.architecture.clean.android.powergit.data.entities.pullrequest

import com.duyp.architecture.clean.android.powergit.data.entities.commit.CommitApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.milestone.MilestoneApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.PullRequestEntity

class PullRequestApiToEntityMapper: Mapper<PullRequestApiData, PullRequestEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mLabelApiToEntityMapper = LabelApiToEntityMapper()

    private val mMileStoneApiToEntity = MilestoneApiToEntityMapper()

    private val mCommitApiToEntityMapper = CommitApiToEntityMapper()

    private val mReactionsApiToEntityMapper = ReactionsApiToEntityMapper()

    override fun mapFrom(e: PullRequestApiData): PullRequestEntity {
        val entity = PullRequestEntity()
        entity.id = e.id
        entity.url = e.url
        entity.body = e.body
        entity.title = e.title
        entity.diffUrl = e.diffUrl
        entity.patchUrl = e.patchUrl
        entity.mergeCommitSha = e.mergeCommitSha
        entity.mergeState = e.mergeState
        entity.repoId = e.repoId
        entity.login = e.login
        entity.state = e.state
        entity.bodyHtml = e.bodyHtml
        entity.htmlUrl = e.htmlUrl
        entity.comments = e.comments
        entity.number = e.number
        entity.commits = e.commits
        entity.additions = e.additions
        entity.deletions = e.deletions
        entity.changedFiles = e.changedFiles
        entity.reviewComments = e.reviewComments
        entity.closedAt = e.closedAt
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.mergedAt = e.mergedAt
        entity.locked = e.locked
        entity.mergable = e.mergable
        entity.merged = e.merged
        entity.mergeable = e.mergeable
        entity.mergedBy = e.mergedBy?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.closedBy = e.closedBy?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.assignee = e.assignee?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.assignees = e.assignees?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.labels = e.labels?.let { mLabelApiToEntityMapper.mapFrom(it) }
        entity.milestone = e.milestone?.let { mMileStoneApiToEntity.mapFrom(it) }
        entity.base = e.base?.let { mCommitApiToEntityMapper.mapFrom(it) }
        entity.head = e.head?.let { mCommitApiToEntityMapper.mapFrom(it) }
        entity.pullRequest = e.pullRequest?.let { this.mapFrom(it) }
        entity.reactions = e.reactions?.let { mReactionsApiToEntityMapper.mapFrom(it) }
        return entity
    }
}