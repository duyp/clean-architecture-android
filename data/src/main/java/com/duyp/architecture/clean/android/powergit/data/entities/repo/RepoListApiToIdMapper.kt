package com.duyp.architecture.clean.android.powergit.data.entities.repo

import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiMapper

class RepoListApiToIdMapper: PageableApiMapper<RepoApiData, Long>() {

    override fun mapItemFrom(apiData: List<RepoApiData>?): List<Long> {
        return apiData?.map { it.id } ?: emptyList()
    }
}