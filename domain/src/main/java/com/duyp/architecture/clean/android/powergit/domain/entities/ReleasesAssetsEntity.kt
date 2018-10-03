package com.duyp.architecture.clean.android.powergit.domain.entities

import java.util.*

data class ReleasesAssetsEntity(

    val id: Long = 0,

    val url: String? = null,

    val browserDownloadUrl: String? = null,

    val name: String? = null,

    val label: String? = null,

    val state: String? = null,

    val contentType: String? = null,

    val size: Int = 0,

    val downloadCount: Int = 0,

    val createdAt: Date? = null,

    val updatedAt: Date? = null,

    val uploader: UserEntity? = null
)