package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.EventRepository
import io.reactivex.Single
import javax.inject.Inject

class GetUserEvent @Inject constructor(
        private val mEventRepository: EventRepository,
        private val mGetUser: GetUser
) {

    /**
     * Get current user received events, requires logged in user.
     */
    fun getMyUserReceivedEvents(page: Int): Single<ListEntity<EventEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { mEventRepository.getUserReceivedEvents(it, page) }

    /**
     * Get current user events
     */
    fun getMyUserEvents(page: Int): Single<ListEntity<EventEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { mEventRepository.getUserEvents(it, page) }

    /**
     * Get received events of given user
     */
    fun getUserReceivedEvents(username: String, page: Int): Single<ListEntity<EventEntity>> =
            mEventRepository.getUserReceivedEvents(username, page)

    /**
     * Get events of given user
     */
    fun getUserEvents(username: String, page: Int): Single<ListEntity<EventEntity>> =
            mEventRepository.getUserEvents(username, page)



}