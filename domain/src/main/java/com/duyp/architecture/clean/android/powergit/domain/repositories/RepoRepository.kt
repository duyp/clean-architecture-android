package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import io.reactivex.Single

interface RepoRepository {

    fun getUserRepoListApi(username: String, filterOptions: FilterOptions, page: Int): Single<ListEntity<RepoEntity>>

    fun getUserRepoListLocal(username: String, filterOptions: FilterOptions): Single<ListEntity<RepoEntity>>
}