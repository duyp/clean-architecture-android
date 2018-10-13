package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRepo
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetUserRepoList
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListViewModel
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
        private val mGetUserRepoList: GetUserRepoList,
        private val mGetRepo: GetRepo
): BasicListViewModel<RepoEntity, Long>(), AdapterData<RepoEntity> {

    private val mFilterOptions = FilterOptions()

    internal var mUsername: String? = null

    override fun refreshAtStartup() = true

    override fun getItem(listItem: Long): RepoEntity {
        return mGetRepo.get(listItem)
                .subscribeOn(Schedulers.io())
                .blockingGet()
                .orElse(null)
    }

    override fun loadList(currentList: ListEntity<Long>): Observable<ListEntity<Long>> {
        return if (mUsername != null)
            mGetUserRepoList.getRepoList(currentList, mUsername!!, mFilterOptions).toObservable()
        else
            mGetUserRepoList.getCurrentUserRepoList(currentList, mFilterOptions).toObservable()
    }

    override fun areItemEquals(old: Long, new: Long): Boolean {
        return old == new
    }
}