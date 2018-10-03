package com.duyp.architecture.clean.android.powergit.domain.entities

class GitCommitEntity(

        var sha: String? = null,

        var url: String? = null,

        var message: String? = null,

        var distincted: Boolean = false,

        var commentCount: Int = 0,

        var author: UserEntity? = null,

        var committer: UserEntity? = null,

        var tree: UserEntity? = null,

        var parents: List<GitCommitEntity>? = null
)