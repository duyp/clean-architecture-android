package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GerUserRepoList
import com.duyp.architecture.clean.android.powergit.ui.base.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListViewModel
import io.reactivex.Observable
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
        private val mGetUserRepoList: GerUserRepoList
): BasicListViewModel<RepoEntity, RepoEntity>(), AdapterData<RepoEntity> {

    private val mFilterOptions = FilterOptions()

    internal var mUsername: String? = null

    override fun getItem(listItem: RepoEntity) = listItem

    override fun loadPageObservable(listEntity: ListEntity<RepoEntity>): Observable<ListEntity<RepoEntity>> {
        return if (mUsername != null)
            mGetUserRepoList.getRepoList(listEntity, mUsername!!, mFilterOptions).toObservable()
        else
            mGetUserRepoList.getCurrentUserRepoList(listEntity, mFilterOptions).toObservable()
    }

}