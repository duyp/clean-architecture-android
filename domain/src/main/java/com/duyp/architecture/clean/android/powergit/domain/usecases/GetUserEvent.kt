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
     * Get current user events (received events), requires logged in user.
     */
    fun getMyUserEvents(): Single<ListEntity<EventEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { mEventRepository.getUserReceivedEvents() }

    /**
     * Get events of given user
     */
    fun getUserEvents(username: String): Single<ListEntity<EventEntity>> =
            mEventRepository.getUserEvents(username)

}