package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Optional
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import io.reactivex.Single
import javax.inject.Inject

class GetIssue @Inject constructor(
        private val mIssueRepository: IssueRepository
) {

    /**
     * Get an [Optional] of issue by given issue id
     */
    fun get(id: Long): Single<Optional<IssueEntity>> =
            mIssueRepository.getById(id)
                    .map { Optional.of(it) }
                    .toSingle(Optional.empty())
                    .onErrorReturnItem(Optional.empty())
}