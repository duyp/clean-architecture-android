package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.repositories.RecentRepoRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRecentRepos @Inject constructor(
        private val mRecentRepoRepository: RecentRepoRepository
) {

    fun search(term: String): Single<List<Long>> =
            mRecentRepoRepository.getRecentRepoIds(term)
                    .onErrorReturnItem(emptyList())
}