package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueQueryProvider.getIssuesQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueQueryProvider.getMyIssueQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.MyIssueTypeEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.mergeWithPreviousPage
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import io.reactivex.Maybe
import javax.inject.Inject

class GetIssueList @Inject constructor(
        private val mIssueRepository: IssueRepository,
        private val mGetUser: GetUser
) {

    /**
     * Get issue list of given [username] with specific [type] and [state]. If username is empty, current logged in
     * username will be used instead
     *
     * @param currentList current issue list which is used to load its next page
     *
     * @return new [ListEntity] which contains all data of [currentList] and new list
     */
    fun getUserIssues(currentList: ListEntity<Long>, username: String?,
                      type: MyIssueTypeEntity, @IssueState state: String) =
            Maybe.fromCallable { username }
                    .switchIfEmpty(mGetUser.getCurrentLoggedInUsername())
                    .flatMap {
                        mIssueRepository.getIssueList(getMyIssueQuery(it, type, state), currentList.getNextPage())
                                .mergeWithPreviousPage(currentList)
                    }!!

    /**
     * Get issue list of given repository with specific [state]
     *
     * @param currentList current issue list which is used to load its next page
     *
     * @return new [ListEntity] which contains all data of [currentList] and new list
     */
    fun getRepoIssues(currentList: ListEntity<Long>, owner: String, repo: String, @IssueState state: String) =
            mIssueRepository.getIssueList(getIssuesQuery(owner, repo, state), currentList.getNextPage())
                    .mergeWithPreviousPage(currentList)
}