package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.issue.GetIssue
import com.duyp.architecture.clean.android.powergit.domain.usecases.issue.GetRecentIssue
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRecentRepos
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRepo
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.SearchPublicRepo
import com.duyp.architecture.clean.android.powergit.domain.usecases.user.GetRecentUser
import com.duyp.architecture.clean.android.powergit.onErrorResumeEmpty
import com.duyp.architecture.clean.android.powergit.printStacktraceIfDebug
import com.duyp.architecture.clean.android.powergit.ui.Event
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val mGetRepo: GetRepo,
        private val mGetIssue: GetIssue,
        private val mGetUser: GetUser,
        private val mGetRecentRepos: GetRecentRepos,
        private val mGetRecentIssue: GetRecentIssue,
        private val mGetRecentUser: GetRecentUser,
        private val mSearchPublicRepo: SearchPublicRepo
): BaseViewModel<SearchState, SearchRepoIntent>(), AdapterData<SearchItem> {

    companion object {
        private const val MIN_SEARCH_TERM_LENGTH = 3
    }

    private var mLoadDisposable: Disposable? = null

    private var mIsLoading = false

    private var mSearchTerm: String = ""

    private var mCurrentTab: Int = 0

    private var mRecentRepoIds: List<Long> = emptyList()

    private var mRecentIssueIds: List<Long> = emptyList()

    private var mRecentUserIds: List<Long> = emptyList()

    private var mSearchResult: RepoSearchResult = RepoSearchResult()

    private var mDataList: ListEntity<SearchItem> = ListEntity()

    override fun initState() = SearchState()

    override fun getItemAtPosition(position: Int): SearchItem? {
        if (mDataList.items.isEmpty() || position < 0 || position >= getTotalCount())
            return null
        return mDataList.items[position]
    }

    override fun getItemTypeAtPosition(position: Int): Int {
        return getItemAtPosition(position)?.viewType() ?: 0
    }

    override fun getTotalCount(): Int {
        return mDataList.items.size
    }

    override fun composeIntent(intentSubject: Observable<SearchRepoIntent>) {

        // subscribe this to set current search term and clear data if needed for any on going search intent
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        mSearchTerm = it.term
                        clearSearchResults(it.term.isEmpty())
                    }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(SearchRepoIntent.SelectTab::class.java)
                    .doOnNext { mCurrentTab = it.tab}
                    .process()
                    .subscribe()
        }

        // load recent repos for any on going search intent
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .filter { !it.term.isEmpty() }
                    .switchMap { loadRecentRepos() }
                    .subscribe()
        }


        // load recent issues for any on going search intent
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .debounce(100L, TimeUnit.MILLISECONDS)
                    .filter { !it.term.isEmpty() }
                    .switchMap { loadRecentIssues() }
                    .subscribe()
        }


        // load recent users for any on going search intent
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .debounce(200L, TimeUnit.MILLISECONDS)
                    .filter { !it.term.isEmpty() }
                    .switchMap { loadRecentUsers() }
                    .subscribe()
        }

        // search public repos for on going search intent debounced with 500ms and only if search term length equal
        // or greater than [MIN_SEARCH_TERM_LENGTH]
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .debounce(500L, TimeUnit.MILLISECONDS)
                    .doOnNext { mSearchTerm = it.term }
                    .filter { mSearchTerm.length >= MIN_SEARCH_TERM_LENGTH }
                    .switchMap { loadSearchResults(true) }
                    .subscribe()
        }

        // load more search result, only for public repos, not for recent repos
        addDisposable {
            intentSubject.ofType(SearchRepoIntent.LoadMore::class.java)
                    .filter { !mIsLoading && mSearchResult.searchResultList.canLoadMore() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { setState { copy(loadingMore = Event.empty()) } }
                    .switchMap { loadSearchResults(false) }
                    .subscribe()
        }
    }

    /**
     * Load recent properties which match current search term
     */
    private fun loadRecentRepos(): Observable<out Any> {
        return mGetRecentRepos.search(mSearchTerm)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { mRecentRepoIds = it }
                .toObservable()
                .process()
    }

    /**
     * Load recent issues which match current search term
     */
    private fun loadRecentIssues(): Observable<out Any> {
        return mGetRecentIssue.getRecentIssueIds(mSearchTerm)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { mRecentIssueIds = it }
                .toObservable()
                .process()
    }

    /**
     * Load recent issues which match current search term
     */
    private fun loadRecentUsers(): Observable<out Any> {
        return mGetRecentUser.getRecentUserIds(mSearchTerm)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { mRecentUserIds = it }
                .toObservable()
                .process()
    }

    /**
     * Load search result of public properties which match current search term
     */
    private fun loadSearchResults(refresh: Boolean): Observable<out Any> {
        val currentList = if (refresh) ListEntity() else mDataList
        return mSearchPublicRepo.search(currentList.copyWith(mSearchResult.searchResultList), mSearchTerm)
                .subscribeOn(Schedulers.io())
                .map { mSearchResult.copy(searchResultList = it, isSearching = false) }
                .toObservable()
                .startWith {
                    it.onNext(mSearchResult.copy(isSearching = true))
                    it.onComplete()
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.fromCallable { mSearchResult.copy(isSearching = false, error = throwable) }
                }
                .doOnNext { mSearchResult = it }
                .process()
    }

    /**
     * Process an observable ([loadRecentRepos] or [loadSearchResults])
     * Result of both [loadRecentRepos] and [loadSearchResults] will be mixed and process to have adapter data to be
     * rendered, as well as calculating diff result and updating state accordingly as result of the loaders
     */
    private fun Observable<out Any>.process(): Observable<out Any> {
        return this.doOnSubscribe {
            mLoadDisposable = it
            mIsLoading = true
        }
                .observeOn(Schedulers.computation())
                .map { createAdapterData() }
                .map { newList ->
                    val diffResult = SearchDiffUtils.calculateDiffResult(mDataList, newList)
                    mDataList = newList
                    return@map diffResult
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    setState {
                        copy(dataUpdated = Event(it))
                    }
                }
                .doOnError {
                    it.printStacktraceIfDebug()
                    mIsLoading = false
                    setState { copy(errorMessage = Event(it.message ?: ""), loadCompleted = Event.empty()) }
                }
                .doOnComplete {
                    mIsLoading = false
                    setState {
                        copy(loadCompleted = Event.empty())
                    }
                }
                .onErrorResumeEmpty()
    }

    @MainThread
    private fun clearSearchResults(clearRecentList: Boolean) {
        if (clearRecentList) {
            mRecentRepoIds = emptyList()
            mRecentIssueIds = emptyList()
            mRecentUserIds = emptyList()
        }
        mSearchResult = RepoSearchResult(
                searchResultList = ListEntity(),
                isSearching = false,
                error = null
        )
        val newList = createAdapterData()
        setState { copy(dataUpdated = Event(SearchDiffUtils.calculateDiffResult(mDataList, newList))) }
        mLoadDisposable?.dispose()
        mDataList = newList
    }

    private fun createAdapterData(): ListEntity<SearchItem> {
        val list = ArrayList<SearchItem>()

        list.add(SearchItem.RecentHeader(mRecentRepoIds.size, mRecentIssueIds.size, mRecentUserIds.size, mCurrentTab))

        when (mCurrentTab) {
            0 -> // recent repos
                if (!mRecentRepoIds.isEmpty()) {
                    list.addAll(mRecentRepoIds.map { SearchItem.RecentRepo(it, mGetRepo) })
                }
            1 -> // recent issues
                if (!mRecentIssueIds.isEmpty()) {
                    list.addAll(mRecentIssueIds.map { SearchItem.RecentIssue(it, mGetIssue) })
                }
            2 -> // recent users
                if (!mRecentUserIds.isEmpty()) {
                    list.addAll(mRecentUserIds.map { SearchItem.RecentUser(it, mGetUser) })
                }
        }

        // search result
        val emptyResult = mSearchResult.searchResultList.items.isEmpty()
        if (mSearchResult.isSearching || mSearchResult.error != null || !emptyResult) {
            list.add(
                    SearchItem.ResultHeader(
                            pageCount = mSearchResult.searchResultList.getPageCount(),
                            loadedCount = mSearchResult.searchResultList.items.size,
                            currentSearchTerm = mSearchTerm,
                            loading = mSearchResult.isSearching,
                            errorMessage = mSearchResult.error?.message
                    )
            )
        }
        if (!emptyResult) {
            list.addAll(mSearchResult.searchResultList.items.map { SearchItem.SearchResultRepo(it) })
        }
        return mSearchResult.searchResultList.copyWith(list)
    }
}

interface SearchRepoIntent {
    data class Search(val term: String): SearchRepoIntent
    data class SelectTab(val tab: Int): SearchRepoIntent
    object LoadMore: SearchRepoIntent
}

data class SearchState(
        val errorMessage: Event<String>? = null,
        val loadingMore: Event<Unit>? = null,
        val loadCompleted: Event<Unit>? = null,
        val dataUpdated: Event<DiffUtil.DiffResult>? = null
)

data class RepoSearchResult(
        val searchResultList: ListEntity<RepoEntity> = ListEntity(),
        val isSearching: Boolean = false,
        val error: Throwable? = null
)