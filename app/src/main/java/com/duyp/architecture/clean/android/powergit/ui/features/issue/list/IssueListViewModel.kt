package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueType
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import com.duyp.architecture.clean.android.powergit.domain.usecases.issue.GetIssueList
import com.duyp.architecture.clean.android.powergit.ui.base.ListIntent
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.ui.base.ListViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class IssueListViewModel @Inject constructor(
        private val mGetIssueList: GetIssueList
): ListViewModel<IssueListState, IssueListIntent, IssueEntity, IssueEntity>() {

    var username: String? = null

    override fun getItem(listItem: IssueEntity) = listItem

    override fun refreshAtStartup() = true

    override fun setListState(s: ListState.() -> ListState) {
        setState { copy(listState = s.invoke(this.listState)) }
    }

    override fun withListState(s: ListState.() -> Unit) {
        withState {
            s.invoke(this.listState)
        }
    }

    override fun loadList(currentList: ListEntity<IssueEntity>): Observable<ListEntity<IssueEntity>> {
        return mGetIssueList.get(currentList, username, IssueType.CREATED, IssueState.OPEN)
                .subscribeOn(Schedulers.io())
                .toObservable()
    }

    override fun areItemEquals(old: IssueEntity, new: IssueEntity) = old.id == new.id

    override fun getRefreshIntent() = IssueListIntent.Refresh

    override fun getLoadMoreIntent() = IssueListIntent.LoadMore

    override fun initState() = IssueListState()

}


data class IssueListState (
        val listState: ListState = ListState()
)


interface IssueListIntent: ListIntent {
    object Refresh: IssueListIntent
    object LoadMore: IssueListIntent
}
