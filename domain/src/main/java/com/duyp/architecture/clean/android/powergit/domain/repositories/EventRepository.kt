package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import io.reactivex.Single

interface EventRepository {

    /**
     * Get self events of specific user with given page number
     */
    fun getUserEvents(username: String, page: Int): Single<ListEntity<EventEntity>>

    /**
     * Get received events of specific user with given page number
     */
    fun getUserReceivedEvents(username: String, page: Int): Single<ListEntity<EventEntity>>

}