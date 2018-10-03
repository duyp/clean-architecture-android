package com.duyp.architecture.clean.android.powergit.data.entities.comment

import com.duyp.architecture.clean.android.powergit.data.entities.reaction.ReactionsApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import java.util.*

data class CommentApiData(

        var id: Long = 0,

        var user: UserApiData? = null,

        var url: String? = null,

        var body: String? = null,

        var bodyHtml: String? = null,

        var htmlUrl: String? = null,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var path: String? = null,

        var commitId: String? = null,

        var repoId: String? = null,

        var login: String? = null,

        var gistId: String? = null,

        var issueId: String? = null,

        var pullRequestId: String? = null,

        var authorAssociation: String? = null,

        var reactions: ReactionsApiData? = null,

        var position: Int = 0,

        var line: Int = 0
)