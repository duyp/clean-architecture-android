package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.mergeWithPreviousPage
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.SearchRepository
import javax.inject.Inject

class SearchPublicRepo @Inject constructor(
        private val mSearchRepository: SearchRepository
) {

    fun search(previousList: ListEntity<RepoEntity>, term: String) =
            mSearchRepository.searchRepos(term, previousList.getNextPage())
                    .mergeWithPreviousPage(previousList)
}