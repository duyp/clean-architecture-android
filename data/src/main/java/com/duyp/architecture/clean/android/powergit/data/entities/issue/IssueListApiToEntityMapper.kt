package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiMapper

class IssueListApiToEntityMapper: PageableApiMapper<IssueApiData, Long>() {

    override fun mapItemFrom(apiData: List<IssueApiData>?): List<Long> {
        return apiData?.map { it.id } ?: emptyList()
    }
}