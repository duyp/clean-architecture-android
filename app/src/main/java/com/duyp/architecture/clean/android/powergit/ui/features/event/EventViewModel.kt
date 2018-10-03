package com.duyp.architecture.clean.android.powergit.ui.features.event

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUserEventList
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListViewModel
import io.reactivex.Observable
import javax.inject.Inject

class EventViewModel @Inject constructor(
        private val mGetUserEventList: GetUserEventList
): BasicListViewModel<EventEntity, EventEntity>() {

    var username: String? = null

    var type: EventType? = EventType.SELF

    override fun getItem(listItem: EventEntity) = listItem

    override fun loadPageObservable(listEntity: ListEntity<EventEntity>): Observable<ListEntity<EventEntity>> {
        val isReceivedEvents = type == EventType.RECEIVED
        return if (username == null) {
            mGetUserEventList.getMyUserEvents(listEntity, isReceivedEvents).toObservable()
        } else {
            mGetUserEventList.getUserEvents(listEntity, username!!, isReceivedEvents).toObservable()
        }
    }
}

enum class EventType {
     SELF,
     RECEIVED
}