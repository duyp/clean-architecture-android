package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import io.reactivex.Single
import javax.inject.Inject

class GetRecentIssue @Inject constructor() {

    fun getRecentIssueIds(searchTerm: String): Single<List<Long>> = Single.just(emptyList())
}