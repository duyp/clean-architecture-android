package com.duyp.architecture.clean.android.powergit.domain.entities

import java.util.*

data class ReleaseEntity(

    var id: Long = 0,

    var url: String? = null,

    var htmlUrl: String? = null,

    var assetsUrl: String? = null,

    var uploadUrl: String? = null,

    var tagName: String? = null,

    var targetCommitish: String? = null,

    var name: String? = null,

    var repoId: String? = null,

    var login: String? = null,

        //@SerializedName("tarball_url")
    var tarballUrl: String? = null,

        //@SerializedName("body_html")
    var body: String? = null,

        //@SerializedName("zipball_url")
    var zipBallUrl: String? = null,

    var draft: Boolean = false,

    var preRelease: Boolean = false,

    var createdAt: Date? = null,

    var publishedAt: Date? = null,

    var author: UserEntity? = null,

    var assets: List<ReleasesAssetsEntity>? = null
)