package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelApiData
import com.duyp.architecture.clean.android.powergit.data.entities.milestone.MilestoneApiData
import com.duyp.architecture.clean.android.powergit.data.entities.pullrequest.PullRequestApiData
import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import java.util.*

data class IssueApiData(

        var id: Long = 0,

        var url: String? = null,

        var body: String? = null,

        var title: String? = null,

        var comments: Int = 0,

        var number: Int = 0,

        var locked: Boolean = false,

        var state: String? = null,

        var repoUrl: String? = null,

        var bodyHtml: String? = null,

        var htmlUrl: String? = null,

        var closedAt: Date? = null,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var user: UserApiData? = null,

        var assignee: UserApiData? = null,

        var closedBy: UserApiData? = null,

        var assignees: List<UserApiData>? = null,

        var labels: List<LabelApiData>? = null,

        var milestone: MilestoneApiData? = null,

        var repository: RepoEntity? = null,

        var pullRequest: PullRequestApiData? = null,

        var reactions: ReactionsApiData? = null
)