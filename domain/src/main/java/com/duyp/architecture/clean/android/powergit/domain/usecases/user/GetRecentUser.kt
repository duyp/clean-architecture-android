package com.duyp.architecture.clean.android.powergit.domain.usecases.user

import io.reactivex.Single
import javax.inject.Inject

class GetRecentUser @Inject constructor() {

    fun getRecentUserIds(searchTerm: String): Single<List<Long>> = Single.just(listOf(1L, 2L, 3L, 4L, 5L))
}