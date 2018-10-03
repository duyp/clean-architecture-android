package com.duyp.architecture.clean.android.powergit.domain.entities

import java.util.*

data class ReleaseAssetEntity(

        var id: Long = 0,

        var url: String? = null,

        var browserDownloadUrl: String? = null,

        var name: String? = null,

        var label: String? = null,

        var state: String? = null,

        var contentType: String? = null,

        var size: Int = 0,

        var downloadCount: Int = 0,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var uploader: UserEntity? = null
)