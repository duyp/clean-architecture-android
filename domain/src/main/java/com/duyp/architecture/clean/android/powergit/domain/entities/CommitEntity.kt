package com.duyp.architecture.clean.android.powergit.domain.entities

data class CommitEntity(

        var sha: String? = null,

        var url: String? = null,

        var message: String? = null,

        var ref: String? = null,

        var commentCount: Int = 0,

        var distincted: Boolean = false,

        var author: UserEntity? = null,

        var committer: UserEntity? = null,

        var tree: UserEntity? = null,

        var parents: List<CommitEntity>? = null,

        var gitCommit: GitCommitEntity? = null
) {

    val user: UserEntity?
        get() = if (author != null) author else gitCommit!!.author
}