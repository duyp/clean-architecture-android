package com.duyp.architecture.clean.android.powergit.data.entities.issue

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData
import java.util.*

@Entity(tableName = "Issue", indices = [Index("title", "user_login")])
data class IssueLocalData(

        @PrimaryKey
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

        var repoOwner: String? = null,

        var repoName: String? = null,

        @Embedded(prefix = "user_")
        var user: UserLocalData? = null,

        // todo check following fields

        @Embedded(prefix = "assignee_")
        var assignee: UserLocalData? = null

//        @Embedded
//        var closedBy: UserLocalData? = null,

//        var assignees: List<UserApiData>? = null,
//
//        var labels: List<LabelApiData>? = null
//
//        var milestone: MilestoneApiData? = null,
//
//        var repository: RepoEntity? = null,
//
//        var pullRequest: PullRequestApiData? = null,
//
//        var reactions: ReactionsApiData? = null
)