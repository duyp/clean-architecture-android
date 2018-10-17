package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity

class IssueListApiToEntityMapper: PageableApiMapper<IssueApiData, IssueEntity>() {

    private val mIssueApiToEntityMapper = IssueApiToEntityMapper()

    override fun mapItemFrom(apiData: List<IssueApiData>?): List<IssueEntity> {
        return mIssueApiToEntityMapper.mapFrom(apiData)
    }
}