package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import io.reactivex.Single

interface EventRepository {

    fun getUserEvents(username: String, page: Int): Single<ListEntity<EventEntity>>

    fun getUserReceivedEvents(username: String, page: Int): Single<ListEntity<EventEntity>>

}