package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.QueryEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.mergeWithPreviousPage
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import javax.inject.Inject

class SearchIssue @Inject constructor(
        private val mIssueRepository: IssueRepository
) {

    /**
     * Search public issues from [currentList] and given [searchTerm]
     *
     * @return new list which contains all issues from page 0 to current page
     */
    fun search(currentList: ListEntity<IssueEntity>, searchTerm: String) =
            mIssueRepository.searchIssues(QueryEntity.getIssueQuery { this.searchTerm = searchTerm }, currentList.getNextPage())
                    .mergeWithPreviousPage(currentList)
}