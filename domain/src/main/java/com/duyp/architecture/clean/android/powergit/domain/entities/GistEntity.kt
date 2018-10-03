package com.duyp.architecture.clean.android.powergit.domain.entities

import java.util.*

data class GistEntity(

    //@SerializedName("nooope")
    var id: Long = 0,

        //@SerializedName("id")
    var gistId: String? = null,

    var url: String? = null,

    var forksUrl: String? = null,

    var commitsUrl: String? = null,

    var gitPullUrl: String? = null,

    var gitPushUrl: String? = null,

    var htmlUrl: String? = null,

    var ownerName: String? = null,

    var description: String? = null,

    var commentsUrl: String? = null,

    var publicX: Boolean = false,

    var truncated: Boolean = false,

    var comments: Int = 0,

    var createdAt: Date? = null,

    var updatedAt: Date? = null,

    //GithubFileModel files;

    var user: UserEntity? = null,

    var owner: UserEntity? = null
)