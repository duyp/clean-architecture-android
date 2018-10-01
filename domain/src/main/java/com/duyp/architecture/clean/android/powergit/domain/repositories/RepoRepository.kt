package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import io.reactivex.Single

interface RepoRepository {

    fun getUserRepoList(username: String, filterOptions: FilterOptions, page: Int): Single<ListEntity<RepoEntity>>
}