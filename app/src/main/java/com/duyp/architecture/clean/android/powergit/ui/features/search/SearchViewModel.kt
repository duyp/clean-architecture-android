package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.RepoSearchResult
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRecentRepos
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRepo
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.SearchPublicRepo
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
        private val mGetRecentRepos: GetRecentRepos,
        private val mSearchPublicRepo: SearchPublicRepo
): BaseViewModel<SearchState, SearchRepoIntent>(), AdapterData<SearchItem> {

    companion object {
        private const val MIN_SEARCH_TERM_LENGTH = 3
    }

    private var mLoadDisposable: Disposable? = null

    private var mIsLoading = false

    private var mSearchTerm: String = ""

    private var mRecentRepoIds: List<Long> = emptyList()

    private var mResultList: ListEntity<RepoEntity> = ListEntity()

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

        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .debounce(500L, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        mSearchTerm = it.term
                        clearSearchResults(it.term.isEmpty())
                    }
                    .filter { !mSearchTerm.isEmpty() }
                    .doOnNext {
                        setState { copy(loadingMore = Event.empty()) }
                    }
                    .switchMap { loadData(true) }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(SearchRepoIntent.LoadMore::class.java)
                    .filter { !mIsLoading }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { setState { copy(loadingMore = Event.empty()) } }
                    .switchMap { loadData(false) }
                    .subscribe()
        }
    }

    private fun loadData(refresh: Boolean): Observable<ListEntity<SearchItem>> {
        val currentList = if (refresh) ListEntity() else mDataList
        return Observable.concatArrayEager(
                if (!refresh) Observable.empty() else mGetRecentRepos.search(mSearchTerm)
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { mRecentRepoIds = it }
                        .map {
                            RepoSearchResult(
                                    recentRepoIds = it,
                                    searchResultList = if (currentList.getNextPage() == ListEntity.STARTING_PAGE)
                                        ListEntity() else currentList.copyWith(mResultList.items))
                        }
                        .toObservable(),
                if (mSearchTerm.length < MIN_SEARCH_TERM_LENGTH) Observable.empty() else
                    mSearchPublicRepo.search(currentList.copyWith(mResultList.items), mSearchTerm)
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { mResultList = it }
                        .map { RepoSearchResult(recentRepoIds = mRecentRepoIds, searchResultList = it) }
                        .toObservable()
        )
                .doOnSubscribe {
                    mLoadDisposable = it
                    mIsLoading = true
                }
                .map { createAdapterData(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    setState {
                        copy(dataUpdated = Event(calculateDiffResult(it)))
                    }
                    mDataList = it
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
        val newList = if (clearRecentList) ListEntity()
        else createAdapterData(RepoSearchResult(recentRepoIds = mRecentRepoIds, searchResultList = ListEntity()))
        setState {
            copy(dataUpdated = Event(calculateDiffResult(newList)))
        }
        mLoadDisposable?.dispose()
        mDataList = newList
    }

    private fun createAdapterData(result: RepoSearchResult): ListEntity<SearchItem> {
        val list = ArrayList<SearchItem>()
        if (!result.recentRepoIds.isEmpty()) {
            list.add(SearchItem.RecentHeader())
            list.addAll(result.recentRepoIds.map { SearchItem.RecentRepo(it, mGetRepo) })
        }
        if (!result.searchResultList.items.isEmpty()) {
            list.add(SearchItem.ResultHeader(
                    result.searchResultList.getPageCount(), result.searchResultList.items.size)
            )
            list.addAll(result.searchResultList.items.map { SearchItem.SearchResultRepo(it) })
        }
        return result.searchResultList.copyWith(list)
    }

    private fun calculateDiffResult(newList: ListEntity<SearchItem>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize() = mDataList.items.size

            override fun getNewListSize() = newList.items.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemEquals(mDataList.items[oldItemPosition], newList.items[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areContentsEquals(mDataList.items[oldItemPosition], newList.items[newItemPosition])
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return null
            }
        }, false)
    }

    private fun areItemEquals(old: SearchItem, new: SearchItem): Boolean {
        if (old.viewType() != new.viewType()) {
            return false
        }
        if (old is SearchItem.RecentHeader || old is SearchItem.ResultHeader) {
            return true
        }
        if (old is SearchItem.RecentRepo) {
            return old.repoId == (new as SearchItem.RecentRepo).repoId
        }
        if (old is SearchItem.SearchResultRepo) {
            return old.repo.id == (new as SearchItem.SearchResultRepo).repo.id
        }
        return false
    }

    private fun areContentsEquals(old: SearchItem, new: SearchItem): Boolean {
        if (old.viewType() != new.viewType()) {
            return false
        }
        if (old is SearchItem.ResultHeader) {
            return old.pageCount == (new as SearchItem.ResultHeader).pageCount
                    && old.loadedCount == new.loadedCount
        }
        if (old is SearchItem.RecentHeader) {
            return true
        }
        if (old is SearchItem.RecentRepo) {
            return old.repoId == (new as SearchItem.RecentRepo).repoId
        }
        if (old is SearchItem.SearchResultRepo) {
            return old.repo == (new as SearchItem.SearchResultRepo).repo
        }
        return false
    }
}

interface SearchItem {

    fun viewType(): Int

    class RecentHeader: SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_RECENT

        override fun toString(): String {
            return "RecentHeader"
        }
    }

    data class ResultHeader(val pageCount: Int, val loadedCount: Int): SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_SEARCH_RESULT
    }

    data class RecentRepo(internal val repoId: Long, private val mGetRepo: GetRepo): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_RECENT

        fun getRepo(): RepoEntity? = mGetRepo.get(repoId)
                .subscribeOn(Schedulers.io())
                .blockingGet()
                .orElse(null)
    }

    data class SearchResultRepo(val repo: RepoEntity): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_SEARCH_RESULT
    }

    companion object {
        const val TYPE_SECTION_RECENT = 0
        const val TYPE_SECTION_SEARCH_RESULT = 1
        const val TYPE_ITEM_RECENT = 2
        const val TYPE_ITEM_SEARCH_RESULT = 3
    }
}

interface SearchRepoIntent {
    data class Search(val term: String): SearchRepoIntent
    object LoadMore: SearchRepoIntent
}

data class SearchState(
        val errorMessage: Event<String>? = null,
        val loadingMore: Event<Unit>? = null,
        val loadCompleted: Event<Unit>? = null,
        val dataUpdated: Event<DiffUtil.DiffResult>? = null
)