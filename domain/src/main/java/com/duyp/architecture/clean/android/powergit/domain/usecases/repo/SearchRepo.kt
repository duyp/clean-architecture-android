package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.RepoSearchResult
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.RecentRepoRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SearchRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchRepo @Inject constructor(
        private val mSearchRepository: SearchRepository,
        private val mRecentRepoRepository: RecentRepoRepository
) {

    fun search(previousResult: RepoSearchResult?, term: String):
            Single<RepoSearchResult> {
        return if (previousResult == null || previousResult.searchResultList.getNextPage() == ListEntity.STARTING_PAGE) {
            mRecentRepoRepository.getRecentRepoIds(term)
                    .flatMap { recentList ->
                        mSearchRepository.searchRepos(term, ListEntity.STARTING_PAGE)
                                .map { distinctSearchResult(recentList, it) }
                                .map { apiList ->
                                    RepoSearchResult(recentRepoIds = recentList, searchResultList = apiList)
                                }
                    }
        } else {
            mSearchRepository.searchRepos(term, previousResult.searchResultList.getNextPage())
                    .map { distinctSearchResult(previousResult.recentRepoIds, it) }
                    .map {
                        RepoSearchResult(
                                recentRepoIds = previousResult.recentRepoIds,
                                searchResultList = it.mergeWith(previousResult.searchResultList)
                        )
                    }
        }
    }

    private fun distinctSearchResult(recentList: List<Long>, result: ListEntity<RepoEntity>): ListEntity<RepoEntity> {
        return result.copy(items = ArrayList(
                result.items.filter { !recentList.contains(it.id) }
        ))
    }
}