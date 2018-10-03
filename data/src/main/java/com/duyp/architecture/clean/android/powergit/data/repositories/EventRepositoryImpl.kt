package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.entities.event.EventListApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.EventRepository
import io.reactivex.Single
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
        private val mUserService: UserService
): EventRepository {

    private val mEventListApiToEntityMapper = EventListApiToEntityMapper()

    override fun getUserEvents(username: String, page: Int): Single<ListEntity<EventEntity>> {
        return mUserService.getUserEvents(username, page)
                .map { mEventListApiToEntityMapper.mapFrom(it) }
    }

    override fun getUserReceivedEvents(username: String, page: Int): Single<ListEntity<EventEntity>> {
        return mUserService.getReceivedEvents(username, page)
                .map { mEventListApiToEntityMapper.mapFrom(it) }
    }

}