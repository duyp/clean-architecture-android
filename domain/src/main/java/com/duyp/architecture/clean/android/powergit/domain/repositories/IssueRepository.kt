package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.QueryEntity
import io.reactivex.Maybe
import io.reactivex.Single

interface IssueRepository {

    /**
     * Search issue by given query
     *
     * See [com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueQueryProvider]
     */
    fun getIssueList(query: QueryEntity, page: Int): Single<ListEntity<Long>>

    /**
     * Search issue stored locally in database
     */
    fun searchLocalIssue(searchTerm: String): Single<List<Long>>

    /**
     * Get issue by given id
     */
    fun getById(id: Long): Maybe<IssueEntity>
}