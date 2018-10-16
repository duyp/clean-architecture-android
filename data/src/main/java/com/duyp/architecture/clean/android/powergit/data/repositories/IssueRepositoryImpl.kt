package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.IssueService
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueListApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import io.reactivex.Single
import javax.inject.Inject

class IssueRepositoryImpl @Inject constructor(
        private val mIssueService: IssueService
): IssueRepository {

    private val mIssueListApiToEntityMapper = IssueListApiToEntityMapper()

    override fun getIssueList(query: String, page: Int): Single<ListEntity<IssueEntity>> {
        return mIssueService.getIssues(query, page)
                .map { mIssueListApiToEntityMapper.mapFrom(it) }
    }
}