package com.duyp.architecture.clean.android.powergit.ui.features.event

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUserEvent
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListViewModel
import io.reactivex.Observable
import javax.inject.Inject

class EventViewModel @Inject constructor(
        private val mGetUserEvent: GetUserEvent
): BasicListViewModel<EventEntity, EventEntity>() {

    var username: String? = null

    var type: EventType? = EventType.SELF

    override fun getItem(listItem: EventEntity) = listItem

    override fun loadPageObservable(page: Int): Observable<ListEntity<EventEntity>> {
        return when (type) {
            EventType.RECEIVED ->
                if (username == null) {
                    mGetUserEvent.getMyUserReceivedEvents(page).toObservable()
                } else {
                    mGetUserEvent.getUserReceivedEvents(username!!, page).toObservable()
                }
            else ->
                if (username == null) {
                    mGetUserEvent.getMyUserEvents(page).toObservable()
                } else {
                    mGetUserEvent.getUserEvents(username!!, page).toObservable()
                }
        }
    }
}

enum class EventType {
     SELF,
     RECEIVED
}