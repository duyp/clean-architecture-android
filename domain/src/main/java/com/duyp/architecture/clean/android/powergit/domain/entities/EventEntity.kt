package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.EventType
import java.util.*

data class EventEntity(

        var id: Long = 0,

        @EventType
        var type: String? = null,

        var createdAt: Date? = null,

        var actor: UserEntity? = null,

        var repo: RepoEntity? = null,

        var payload: PayloadEntity? = null,

        var publicEvent: Boolean = false,

        // not a response field from github api, used to identify this event is belong to whom (user received events)
        var receivedOwner: String? = null
)