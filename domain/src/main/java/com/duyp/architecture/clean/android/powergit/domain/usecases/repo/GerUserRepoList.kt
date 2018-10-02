package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.RepoRepository
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import io.reactivex.Single
import javax.inject.Inject

class GerUserRepoList @Inject constructor(
        private val mRepoRepository: RepoRepository,
        private val mGetUser: GetUser
) {

    fun getRepoList(username: String, filterOptions: FilterOptions, page: Int): Single<ListEntity<RepoEntity>> =
            mRepoRepository.getUserRepoListApi(username, filterOptions, page)
                    .onErrorResumeNext { throwable ->
                        if (page == ListEntity.STARTING_PAGE) {
                            // api error when loading first page, let load from database
                            mRepoRepository.getUserRepoListLocal(username, filterOptions)
                        } else {
                            Single.error(throwable)
                        }
                    }

    fun getCurrentUserRepoList(filterOptions: FilterOptions, page: Int): Single<ListEntity<RepoEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { username ->
                        mRepoRepository.getUserRepoListApi(null, filterOptions, page)
                                .onErrorResumeNext { throwable ->
                                    if (page == ListEntity.STARTING_PAGE) {
                                        // api error when loading first page, let load from database
                                        mRepoRepository.getUserRepoListLocal(username, filterOptions)
                                    } else {
                                        Single.error(throwable)
                                    }
                                }
                    }
}