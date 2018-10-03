package com.duyp.architecture.clean.android.powergit.data.entities.commit

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.google.gson.annotations.SerializedName

data class CommitApiData(

        var sha: String? = null,

        var url: String? = null,

        var message: String? = null,

        var ref: String? = null,

        var commentCount: Int = 0,

        @SerializedName("distinct")
        var distincted: Boolean = false,

        var author: UserApiData? = null,

        var committer: UserApiData? = null,

        var tree: UserApiData? = null,

        var parents: List<CommitApiData>? = null,

        @SerializedName("commit")
        var gitCommit: GitCommitApiData? = null
)