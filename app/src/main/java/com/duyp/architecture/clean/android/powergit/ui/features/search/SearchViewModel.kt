package com.duyp.architecture.clean.android.powergit.ui.features.search

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.RepoSearchResult
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRecentRepos
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRepo
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.SearchPublicRepo
import com.duyp.architecture.clean.android.powergit.ui.base.ListIntent
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.ui.base.ListViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val mGetRepo: GetRepo,
        private val mGetRecentRepos: GetRecentRepos,
        private val mSearchPublicRepo: SearchPublicRepo
): ListViewModel<ListState, SearchRepoIntent, SearchItem, SearchItem>() {

    private var mSearchTerm: String = ""

    private var mRecentRepoIds: List<Long> = emptyList()

    private var mResultList: ListEntity<RepoEntity> = ListEntity()

    override fun refreshAtStartup() = false

    override fun setListState(s: ListState.() -> ListState) {
        setState(s)
    }

    override fun withListState(s: ListState.() -> Unit) {
        withState(s)
    }

    override fun getRefreshIntent() = SearchRepoIntent.Refresh

    override fun getLoadMoreIntent() = SearchRepoIntent.LoadMore

    override fun initState() = ListState(refreshable = false)

    override fun getItem(listItem: SearchItem) = listItem

    override fun getItemType(listItem: SearchItem) = listItem.viewType()

    override fun composeIntent(intentSubject: Observable<SearchRepoIntent>) {
        super.composeIntent(intentSubject)

        setState { this }

        addDisposable {
            intentSubject.ofType(SearchRepoIntent.Search::class.java)
                    .debounce(300L, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        mSearchTerm = it.term
                        if (mSearchTerm.isEmpty()) {
                            clearResults()
                        }
                    }
                    .filter { !mSearchTerm.isEmpty() }
                    .switchMap { loadData(true) }
                    .subscribe()
        }
    }

    override fun checkRefreshable(): Boolean {
        return !mSearchTerm.isEmpty()
    }

    override fun loadList(currentList: ListEntity<SearchItem>): Observable<ListEntity<SearchItem>> {
        return Observable.concatArrayEager(
                mGetRecentRepos.search(mSearchTerm)
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { mRecentRepoIds = it }
                        .map {
                            RepoSearchResult(
                                    recentRepoIds = it,
                                    searchResultList = if (currentList.getNextPage() == ListEntity.STARTING_PAGE)
                                        ListEntity() else currentList.copyWith(mResultList.items))
                        }
                        .toObservable(),
                mSearchPublicRepo.search(currentList.copyWith(mResultList.items), mSearchTerm)
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { mResultList = it }
                        .map { RepoSearchResult(recentRepoIds = mRecentRepoIds, searchResultList = it) }
                        .toObservable()
        )
                .map { result ->
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
                    return@map result.searchResultList.copyWith(list)
                }
    }

    override fun areItemEquals(old: SearchItem, new: SearchItem): Boolean {
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

    override fun areContentsEquals(old: SearchItem, new: SearchItem): Boolean {
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

    companion object {
        const val TYPE_SECTION_RECENT = 0
        const val TYPE_SECTION_SEARCH_RESULT = 1
        const val TYPE_ITEM_RECENT = 2
        const val TYPE_ITEM_SEARCH_RESULT = 3
    }

    fun viewType(): Int

    class RecentHeader: SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_RECENT
    }

    data class ResultHeader(
            val pageCount: Int,
            val loadedCount: Int
    ): SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_SEARCH_RESULT
    }

    data class RecentRepo(internal val repoId: Long,
                          private val mGetRepo: GetRepo): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_RECENT

        fun getRepo(): RepoEntity? = mGetRepo.get(repoId)
                .subscribeOn(Schedulers.io())
                .blockingGet()
                .orElse(null)
    }

    data class SearchResultRepo(val repo: RepoEntity): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_SEARCH_RESULT
    }
}

interface SearchRepoIntent: ListIntent {
    object LoadMore: SearchRepoIntent
    object Refresh: SearchRepoIntent
    data class Search(val term: String): SearchRepoIntent
}

