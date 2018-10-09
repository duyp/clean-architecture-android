package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import io.reactivex.Single

interface SearchRepository {

    /**
     * Search public repositories with github api
     */
    fun searchRepos(term: String, page: Int): Single<ListEntity<RepoEntity>>
}