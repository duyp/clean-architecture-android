package com.duyp.architecture.clean.android.powergit.data.entities.pullrequest

import com.duyp.architecture.clean.android.powergit.data.entities.commit.CommitApiData
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelApiData
import com.duyp.architecture.clean.android.powergit.data.entities.milestone.MilestoneApiData
import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import java.util.*

class PullRequestApiData(

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

        var state: String? = null,

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

        var mergedBy: UserApiData? = null,

        var closedBy: UserApiData? = null,

        var user: UserApiData? = null,

        var assignee: UserApiData? = null,

        var assignees: List<UserApiData>? = null,

        var labels: List<LabelApiData>? = null,

        var milestone: MilestoneApiData? = null,

        var base: CommitApiData? = null,

        var head: CommitApiData? = null,

        var pullRequest: PullRequestApiData? = null,

        var reactions: ReactionsApiData? = null
)