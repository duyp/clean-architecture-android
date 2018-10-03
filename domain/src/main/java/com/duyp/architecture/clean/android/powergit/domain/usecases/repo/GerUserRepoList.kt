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

    fun getRepoList(listEntity: ListEntity<RepoEntity>, username: String, filterOptions: FilterOptions):
            Single<ListEntity<RepoEntity>> =
            mRepoRepository.getUserRepoListApi(username, filterOptions, listEntity.next)
                    .onErrorResumeNext { throwable ->
                        if (listEntity.nextIsFirstPage()) {
                            // api error when loading first page, let load from database
                            mRepoRepository.getUserRepoListLocal(username, filterOptions)
                                    .map { it.copy(apiError = throwable) }
                        } else {
                            Single.error(throwable)
                        }
                    }
                    .map { it.copy(items = listEntity.items + it.items) }

    fun getCurrentUserRepoList(listEntity: ListEntity<RepoEntity>, filterOptions: FilterOptions):
            Single<ListEntity<RepoEntity>> =
            mGetUser.getCurrentLoggedInUsername()
                    .flatMap { username ->
                        mRepoRepository.getUserRepoListApi(null, filterOptions, listEntity.next)
                                .onErrorResumeNext { throwable ->
                                    if (listEntity.nextIsFirstPage()) {
                                        // api error when loading first page, let load from database
                                        mRepoRepository.getUserRepoListLocal(username, filterOptions)
                                                .map { it.copy(apiError = throwable) }
                                    } else {
                                        Single.error(throwable)
                                    }
                                }
                    }
                    .map { it.copy(items = listEntity.items + it.items) }
}