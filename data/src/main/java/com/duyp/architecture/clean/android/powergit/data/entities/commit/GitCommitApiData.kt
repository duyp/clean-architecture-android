package com.duyp.architecture.clean.android.powergit.data.entities.commit

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.google.gson.annotations.SerializedName

class GitCommitApiData(

        var sha: String? = null,

        var url: String? = null,

        var message: String? = null,

        @SerializedName("distinct")
        var distincted: Boolean = false,

        var commentCount: Int = 0,

        var author: UserApiData? = null,

        var committer: UserApiData? = null,

        var tree: UserApiData? = null,

        var parents: List<GitCommitApiData>? = null
)