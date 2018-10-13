package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.database.RepoDao
import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoListApiToIdMapper
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.RepoRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
        private val mRepoDao: RepoDao,
        private val mUserService: UserService
) : RepoRepository {

    private val mRepoListApiToIdMapper = RepoListApiToIdMapper()

    private val mRepoApiToLocalMapper = RepoApiToLocalMapper()

    private val mRepoLocalToEntityMapper = RepoLocalToEntityMapper()

    override fun getUserRepoList(username: String, filterOptions: FilterOptions, page: Int):
            Single<ListEntity<Long>> {
        return mUserService.getRepos(username, filterOptions.getQueryMap(), page)
                .processApiRepoList(username, page == ListEntity.STARTING_PAGE)
    }

    override fun getMyUserRepoList(username: String, filterOptions: FilterOptions, page: Int):
            Single<ListEntity<Long>> {
        return mUserService.getMyRepos(filterOptions.getQueryMap(), page)
                .processApiRepoList(username, page == ListEntity.STARTING_PAGE)
    }

    override fun getById(id: Long): Maybe<RepoEntity> {
        return mRepoDao.getById(id)
                .map { mRepoLocalToEntityMapper.mapFrom(it) }
    }

    /**
     * Save repo list api response. Load local data if api error and is first page
     */
    private fun Single<PageableApiData<RepoApiData>>.processApiRepoList(username: String, isStartingPage: Boolean):
            Single<ListEntity<Long>> {
        return this
                .doOnSuccess {
                    // save to database
                    mRepoDao.insertList(mRepoApiToLocalMapper.mapFrom(it.items))
                }
                .map { mRepoListApiToIdMapper.mapFrom(it) }
                .onErrorResumeNext { throwable ->
                    if (isStartingPage) {
                        // api error when loading first page, let load from database
                        return@onErrorResumeNext mRepoDao.getUserRepoIds(username)
                                .map { ListEntity(items = it, isOfflineData = true, apiError = throwable) }
                    } else {
                        return@onErrorResumeNext Single.error(throwable)
                    }
                }
    }
}