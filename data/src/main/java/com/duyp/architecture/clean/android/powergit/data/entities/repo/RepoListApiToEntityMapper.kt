package com.duyp.architecture.clean.android.powergit.data.entities.repo

import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity

class RepoListApiToEntityMapper: PageableApiMapper<RepoApiData, RepoEntity>() {

    private val mRepoApiToEntityMapper = RepoApiToEntityMapper()

    override fun mapItemFrom(apiData: List<RepoApiData>?): List<RepoEntity> {
        return mRepoApiToEntityMapper.mapFrom(apiData)
    }
}