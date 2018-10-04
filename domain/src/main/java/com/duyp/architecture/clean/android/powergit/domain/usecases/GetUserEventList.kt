package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.mergeWithPreviousPage
import com.duyp.architecture.clean.android.powergit.domain.repositories.EventRepository
import io.reactivex.Single
import javax.inject.Inject

class GetUserEventList @Inject constructor(
        private val mEventRepository: EventRepository,
        private val mGetUser: GetUser
) {

    /**
     * Get event list of current logged in user, see [getUserEvents]
     */
    fun getMyUserEvents(list: ListEntity<EventEntity>, receivedEvents: Boolean): Single<ListEntity<EventEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { getUserEvents(list, it, receivedEvents) }

    /**
     * Get event list of given user. The next page if current list will be load and appended with previous list.
     *
     * @param currentList page to load
     * @param receivedEvents true if get received event, else get self event
     *
     * @return new list which contains all items from starting page to current page
     */
    fun getUserEvents(currentList: ListEntity<EventEntity>, username: String, receivedEvents: Boolean):
            Single<ListEntity<EventEntity>> =
            (if (receivedEvents) mEventRepository.getUserReceivedEvents(username, currentList.next)
            else mEventRepository.getUserEvents(username, currentList.next))
                    .mergeWithPreviousPage(currentList)

}