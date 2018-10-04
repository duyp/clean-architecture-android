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

    override fun loadList(currentList: ListEntity<EventEntity>): Observable<ListEntity<EventEntity>> {
        val isReceivedEvents = type == EventType.RECEIVED
        return if (username == null) {
            mGetUserEventList.getMyUserEvents(currentList, isReceivedEvents).toObservable()
        } else {
            mGetUserEventList.getUserEvents(currentList, username!!, isReceivedEvents).toObservable()
        }
    }

    override fun areItemEquals(old: EventEntity, new: EventEntity): Boolean {
        return old.id == new.id
    }
}

enum class EventType {
     SELF,
     RECEIVED
}