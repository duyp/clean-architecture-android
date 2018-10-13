package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Optional
import io.reactivex.Single
import javax.inject.Inject

class GetIssue @Inject constructor() {

    fun get(id: Long): Single<Optional<IssueEntity>> = Single.just(Optional.empty())
}