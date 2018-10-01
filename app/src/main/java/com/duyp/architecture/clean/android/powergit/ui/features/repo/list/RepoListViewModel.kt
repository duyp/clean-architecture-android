package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GerUserRepoList
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import io.reactivex.Observable
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
        private val mGetUserRepoList: GerUserRepoList
): BasicListViewModel<RepoEntity>(), AdapterData<RepoEntity> {

    private val mFilterOptions = FilterOptions()

    override fun loadPageObservable(page: Int): Observable<ListEntity<RepoEntity>> {
        return mGetUserRepoList.getRepoList("duyp", mFilterOptions, page)
                .toObservable()
    }
}