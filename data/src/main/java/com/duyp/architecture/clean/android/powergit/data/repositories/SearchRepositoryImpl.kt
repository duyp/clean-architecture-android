package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.SearchService
import com.duyp.architecture.clean.android.powergit.data.database.RepoDao
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoListApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.SearchRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
        private val mSearchService: SearchService,
        private val mRepoDao: RepoDao
): SearchRepository {

    private val mRepoListApiToEntityMapper = RepoListApiToEntityMapper()

    private val mRepoApiToLocalMapper = RepoApiToLocalMapper()

    override fun searchRepos(term: String, page: Int): Single<ListEntity<RepoEntity>> {
        return mSearchService.searchRepositories(term, page)
                .doOnSuccess {
                    // update if existed in database
                    it.items.forEach { repo ->
                        if (mRepoDao.countRepo(repo.id) > 0) {
                            mRepoDao.insert(mRepoApiToLocalMapper.mapFrom(repo))
                        }
                    }
                }
                .map { mRepoListApiToEntityMapper.mapFrom(it) }
    }
}