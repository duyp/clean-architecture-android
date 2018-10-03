package com.duyp.architecture.clean.android.powergit.domain.entities

data class CommitEntity(

    var sha: String? = null,

    var url: String? = null,

    var message: String? = null,

    var ref: String? = null,

    var commentCount: Int = 0,

    //@SerializedName("distinct")
    var distincted: Boolean = false,

    var author: UserEntity? = null,

    var committer: UserEntity? = null,

    var tree: UserEntity? = null,

    var parents: ListEntity<CommitEntity>? = null,

    //@SerializedName("commit")
    var gitCommit: GitCommitEntity? = null
) {

    val user: UserEntity?
        get() = if (author != null) author else gitCommit!!.author
}