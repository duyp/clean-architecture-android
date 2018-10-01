package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.repositories.RepoRepository
import javax.inject.Inject

class GerUserRepoList @Inject constructor(
        private val mRepoRepository: RepoRepository
) {

    fun getRepoList(username: String, filterOptions: FilterOptions, page: Int) =
            mRepoRepository.getUserRepoList(username, filterOptions, page)

}