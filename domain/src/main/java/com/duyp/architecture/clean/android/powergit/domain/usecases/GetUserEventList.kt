package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.EventRepository
import io.reactivex.Single
import javax.inject.Inject

class GetUserEventList @Inject constructor(
        private val mEventRepository: EventRepository,
        private val mGetUser: GetUser
) {

    /**
     * Get current user events, requires logged in user.
     *
     * @param page page to load
     * @param receivedEvents true if get received event, else get self event
     */
    fun getMyUserEvents(page: Int, receivedEvents: Boolean): Single<ListEntity<EventEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { getUserEvents(page, it, receivedEvents) }

    /**
     * Get events of given user
     *
     * @param page page to load
     * @param receivedEvents true if get received event, else get self event
     */
    fun getUserEvents(page: Int, username: String, receivedEvents: Boolean):
            Single<ListEntity<EventEntity>> =
            (if (receivedEvents) mEventRepository.getUserReceivedEvents(username, page)
            else mEventRepository.getUserEvents(username, page))

}