package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.MyIssueType
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

    internal lateinit var listType: IssueListType

    // for repo's issue list
    private lateinit var mRepoOwner: String

    private lateinit var mRepoName: String

    // for user's issue list
    private var mUserName: String? = null

    override fun getItem(listItem: IssueEntity) = listItem

    override fun refreshAtStartup() = true

    override fun setListState(s: ListState.() -> ListState) {
        setState { copy(listState = s.invoke(this.listState)) }
    }

    override fun withListState(s: ListState.() -> Unit) {
        withState { s.invoke(this.listState) }
    }

    override fun loadList(currentList: ListEntity<IssueEntity>): Observable<ListEntity<IssueEntity>> {
        return if (listType == IssueListType.USER_ISSUES)
            mGetIssueList.getUserIssues(currentList, mUserName, MyIssueType.CREATED, IssueState.OPEN)
                .subscribeOn(Schedulers.io())
                .toObservable()
        else
            mGetIssueList.getRepoIssues(currentList, mRepoOwner, mRepoName, IssueState.OPEN)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
    }

    override fun areItemEquals(old: IssueEntity, new: IssueEntity) = old.id == new.id

    override fun getRefreshIntent() = IssueListIntent.Refresh

    override fun getLoadMoreIntent() = IssueListIntent.LoadMore

    override fun initState() = IssueListState(myIssueType = MyIssueType.CREATED)

    /**
     * Init this view model with repo issues type
     */
    internal fun initWithRepo(owner: String, repoName: String) {
        mRepoOwner = owner
        mRepoName = repoName
        listType = IssueListType.REPO_ISSUES
    }

    /**
     * Init this view model with user issues type
     *
     * @param username null if is current user
     */
    internal fun initWithUser(username: String?) {
        mUserName = username
        listType = IssueListType.USER_ISSUES
    }
}

enum class IssueListType {
    USER_ISSUES,
    REPO_ISSUES
}

data class IssueListState (
        val listState: ListState = ListState(),
        val myIssueType: MyIssueType
)


interface IssueListIntent: ListIntent {
    object Refresh: IssueListIntent
    object LoadMore: IssueListIntent
}
