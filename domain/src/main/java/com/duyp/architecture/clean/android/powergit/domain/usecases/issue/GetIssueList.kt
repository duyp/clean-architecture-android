package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueQueryProvider.getQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueState
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueType
import com.duyp.architecture.clean.android.powergit.domain.entities.mergeWithPreviousPage
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import javax.inject.Inject

class GetIssueList @Inject constructor(
        private val mIssueRepository: IssueRepository
) {

    /**
     * Get issue list of given [username] with specific [type] and [state]
     *
     * @param currentList current issue list which is used to load its next page
     *
     * @return new [ListEntity] which contains all data of [currentList] and new list
     */
    fun get(currentList: ListEntity<IssueEntity>, username: String, type: IssueType, state: IssueState) =
            mIssueRepository.getIssueList(getQuery(username, type, state), currentList.getNextPage())
                    .mergeWithPreviousPage(currentList)
}