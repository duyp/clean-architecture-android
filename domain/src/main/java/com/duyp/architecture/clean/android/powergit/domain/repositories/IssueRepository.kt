package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import io.reactivex.Single

interface IssueRepository {

    fun getIssueList(query: String, page: Int): Single<ListEntity<IssueEntity>>
}