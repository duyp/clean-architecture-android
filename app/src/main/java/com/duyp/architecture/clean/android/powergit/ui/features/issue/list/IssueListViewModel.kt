package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.MyIssueTypeEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import com.duyp.architecture.clean.android.powergit.domain.usecases.issue.GetIssueList
import com.duyp.architecture.clean.android.powergit.ui.base.ListIntent
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.ui.base.ListViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    override fun composeIntent(intentSubject: Observable<IssueListIntent>) {
        super.composeIntent(intentSubject)

        addDisposable {
            intentSubject.ofType(IssueListIntent.MyIssueTypeSelected::class.java)
                    .filter {
                        it.type != state().myIssueType
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        clearResults()
                        setState { copy(myIssueType = it.type) }
                    }
                    .switchMap { loadData(true) }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(IssueListIntent.StateSwitch::class.java)
                    .filter {
                        it.isOpenIssue != state().isOpenIssue
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        clearResults()
                        setState { copy(isOpenIssue = it.isOpenIssue) }
                    }
                    .switchMap { loadData(true) }
                    .subscribe()
        }
    }

    override fun loadList(currentList: ListEntity<IssueEntity>): Observable<ListEntity<IssueEntity>> {
        return withState {
            val issueState = if (isOpenIssue) IssueState.OPEN else IssueState.CLOSED
            return@withState if (listType == IssueListType.USER_ISSUES)
                mGetIssueList.getUserIssues(currentList, mUserName, myIssueType.toEntity(), issueState)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
            else
                mGetIssueList.getRepoIssues(currentList, mRepoOwner, mRepoName, issueState)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
        }
    }

    override fun areItemEquals(old: IssueEntity, new: IssueEntity) = old.id == new.id

    override fun getRefreshIntent() = IssueListIntent.Refresh

    override fun getLoadMoreIntent() = IssueListIntent.LoadMore

    override fun initState() = IssueListState(myIssueType = MyIssueType.CREATED, isOpenIssue = true)

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
        val myIssueType: MyIssueType,
        val isOpenIssue: Boolean
)


interface IssueListIntent: ListIntent {
    object Refresh: IssueListIntent
    object LoadMore: IssueListIntent
    data class MyIssueTypeSelected(val type: MyIssueType): IssueListIntent
    data class StateSwitch(val isOpenIssue: Boolean): IssueListIntent
}

enum class MyIssueType(val stringId: Int) {
    CREATED(R.string.created),
    ASSIGNED(R.string.assigned),
    MENTIONED(R.string.mentioned),
    PARTICIPATED(R.string.participated);

    fun toEntity(): MyIssueTypeEntity {
        return when (this) {
            CREATED -> MyIssueTypeEntity.CREATED
            ASSIGNED -> MyIssueTypeEntity.ASSIGNED
            MENTIONED -> MyIssueTypeEntity.MENTIONED
            PARTICIPATED -> MyIssueTypeEntity.PARTICIPATED
        }
    }

    fun getPosition(): Int {
        return when (this) {
            CREATED -> 0
            ASSIGNED -> 1
            MENTIONED -> 2
            PARTICIPATED -> 3
        }
    }

    companion object {

        /**
         * Get list string resoure ids
         */
        fun listStringIds() = MyIssueType.values().map { it.stringId }

        /**
         * Get from position (spinner)
         */
        fun of(position: Int): MyIssueType {
            return when (position) {
                0 -> CREATED
                1 -> ASSIGNED
                2 -> MENTIONED
                else -> PARTICIPATED
            }
        }
    }
}