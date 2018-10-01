package com.duyp.architecture.clean.android.powergit.data.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

abstract class PageableApiMapper<ApiType, ToType>: Mapper<PageableApiData<ApiType>, ListEntity<ToType>>() {

    override fun mapFrom(e: PageableApiData<ApiType>): ListEntity<ToType> {
        return ListEntity(
                first = e.first,
                next = e.next,
                prev = e.prev,
                last = e.last,
                totalCount = e.totalCount,
                incompleteResults = e.incompleteResults,
                items = mapItemFrom(e.items),
                isApiData = true
        )
    }

    abstract fun mapItemFrom(apiData: List<ApiType>?): List<ToType>
}