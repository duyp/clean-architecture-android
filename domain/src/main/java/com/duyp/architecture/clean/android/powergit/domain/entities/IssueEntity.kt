package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import java.util.*

data class IssueEntity(

        var id: Long = 0,

        var url: String? = null,

        var body: String? = null,

        var title: String? = null,

        var comments: Int = 0,

        var number: Int = 0,

        var locked: Boolean = false,

        @IssueState
        var state: String? = null, //todo IssueState2

        var repoUrl: String? = null,

        var bodyHtml: String? = null,

        var htmlUrl: String? = null,

        var closedAt: Date? = null,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var repoName: String? = null,

        var login: String? = null,

        var user: UserEntity? = null,

        var assignee: UserEntity? = null,

        var closedBy: UserEntity? = null,

        var assignees: List<UserEntity>? = null,

        var labels: List<LabelEntity>? = null,

        var milestone: MilestoneEntity? = null,

        var repository: RepoEntity? = null,

        var pullRequest: PullRequestEntity? = null,

        var reactions: ReactionsEntity? = null
)