package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import java.util.*

class PullRequestEntity(

    var id: Long = 0,

    var url: String? = null,

    var body: String? = null,

    var title: String? = null,

    var diffUrl: String? = null,

    var patchUrl: String? = null,

    var mergeCommitSha: String? = null,

    var mergeState: String? = null,

    var repoId: String? = null,

    var login: String? = null,

    @IssueState
    var state: String? = null, // todo IssueState2

    var bodyHtml: String? = null,

    var htmlUrl: String? = null,

    var comments: Int = 0,

    var number: Int = 0,

    var commits: Int = 0,

    var additions: Int = 0,

    var deletions: Int = 0,

    var changedFiles: Int = 0,

    var reviewComments: Int = 0,

    var closedAt: Date? = null,

    var createdAt: Date? = null,

    var updatedAt: Date? = null,

    var mergedAt: Date? = null,

    var locked: Boolean = false,

    var mergable: Boolean = false,

    var merged: Boolean = false,

    var mergeable: Boolean = false,

    var mergedBy: UserEntity? = null,

    var closedBy: UserEntity? = null,

    var user: UserEntity? = null,

    var assignee: UserEntity? = null,

    var assignees: List<UserEntity>? = null,

    var labels: List<LabelEntity>? = null,

    var milestone: MilestoneEntity? = null,

    var base: CommitEntity? = null,

    var head: CommitEntity? = null,

    var pullRequest: PullRequestEntity? = null,

    var reactions: ReactionsEntity? = null
)