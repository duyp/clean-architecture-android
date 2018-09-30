package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoListEntity
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class RepoListViewModel @Inject constructor(): BaseViewModel<RepoListState, RepoListIntent>() {

    override fun initState() = RepoListState()

    override fun composeIntent(intentSubject: Observable<RepoListIntent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

data class RepoListState(
        val repoListEntity: RepoListEntity? = null
)

sealed class RepoListIntent {
    class LoadMoreIntent
}
