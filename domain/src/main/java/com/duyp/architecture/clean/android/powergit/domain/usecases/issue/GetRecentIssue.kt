package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRecentIssue @Inject constructor(
        private val mIssueRepository: IssueRepository
) {

    fun getRecentIssueIds(searchTerm: String): Single<List<Long>> =
            mIssueRepository.searchLocalIssues(searchTerm)
}