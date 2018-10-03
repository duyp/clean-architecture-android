package com.duyp.architecture.clean.android.powergit.data.entities.event

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.EventType
import com.google.gson.annotations.SerializedName
import java.util.*

data class EventApiData(

        var id: Long = 0,

        @EventType
        var type: String? = null,

        @SerializedName("created_at")
        var createdAt: Date? = null,

        var actor: UserApiData? = null,

        var repo: RepoEntity? = null,

        var payload: PayloadApiData? = null,

        @SerializedName("public")
        var publicEvent: Boolean = false
)
