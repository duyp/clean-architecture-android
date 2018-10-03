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

    /**
     * Get repo list of given user with given filter options for specific page
     *
     * @param page page to load
     * @param filterOptions option to filter result
     */
    fun getRepoList(page: Int, username: String, filterOptions: FilterOptions):
            Single<ListEntity<RepoEntity>> =
            mRepoRepository.getUserRepoListApi(username, filterOptions, page)
                    .onErrorResumeNext { throwable ->
                        if (page == ListEntity.STARTING_PAGE) {
                            // api error when loading first page, let load from database
                            mRepoRepository.getUserRepoListLocal(username, filterOptions)
                                    .map { it.copy(apiError = throwable) }
                        } else {
                            Single.error(throwable)
                        }
                    }

    /**
     * Get repo list of current logged in user with given filter options for specific page
     *
     * @param page page to load
     * @param filterOptions option to filter result
     */
    fun getCurrentUserRepoList(page: Int, filterOptions: FilterOptions):
            Single<ListEntity<RepoEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { username ->
                        mRepoRepository.getUserRepoListApi(null, filterOptions, page)
                                .onErrorResumeNext { throwable ->
                                    if (page == ListEntity.STARTING_PAGE) {
                                        // api error when loading first page, let load from database
                                        mRepoRepository.getUserRepoListLocal(username, filterOptions)
                                                .map { it.copy(apiError = throwable) }
                                    } else {
                                        Single.error(throwable)
                                    }
                                }
                    }
}