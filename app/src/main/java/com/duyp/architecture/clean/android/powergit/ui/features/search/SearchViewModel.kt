package com.duyp.architecture.clean.android.powergit.ui.features.search

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.RepoSearchResult
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.SearchRepo
import com.duyp.architecture.clean.android.powergit.ui.base.BasicListViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val mSearchRepo: SearchRepo
): BasicListViewModel<SearchItem, SearchItem>() {

    override fun getItem(listItem: SearchItem) = listItem

    override fun getItemType(listItem: SearchItem) = listItem.viewType()

    override fun loadList(currentList: ListEntity<SearchItem>): Observable<ListEntity<SearchItem>> {

        // revert recent list
        val recentList = currentList.items.asSequence()
                .filter { it is SearchItem.RecentRepo }
                .map { (it as SearchItem.RecentRepo).repoId }
                .toList()

        // revert result list
        val currentResultList = currentList.copyWith(
                currentList.items.asSequence()
                        .filter { it is SearchItem.SearchRepo }
                        .map { (it as SearchItem.SearchRepo).repo }
                        .toList()
        )
        return mSearchRepo.search(RepoSearchResult(recentList, currentResultList), "abcd")
                .map { result ->
                    val list = ArrayList<SearchItem>()
                    if (!result.recentRepoIds.isEmpty()) {
                        list.add(SearchItem.RecentHeader())
                        list.addAll(result.recentRepoIds.map { SearchItem.RecentRepo(it) })
                    }
                    if (!result.searchResultList.items.isEmpty()) {
                        list.add(SearchItem.ResultHeader(result.searchResultList.items.size))
                        list.addAll(result.searchResultList.items.map { SearchItem.SearchRepo(it) })
                    }
                    return@map result.searchResultList.copyWith(list)
                }
                .toObservable()
    }

    override fun areItemEquals(old: SearchItem, new: SearchItem): Boolean {
        if (old.viewType() != new.viewType()) {
            return false
        }
        if (old is SearchItem.ResultHeader || old is SearchItem.ResultHeader) {
            return true
        }
        if (old is SearchItem.RecentRepo) {
            return old.repoId == (new as SearchItem.RecentRepo).repoId
        }
        if (old is SearchItem.SearchRepo) {
            return old.repo.id == (new as SearchItem.SearchRepo).repo.id
        }
        return false
    }

    override fun areContentsEquals(old: SearchItem, new: SearchItem): Boolean {
        if (old.viewType() != new.viewType()) {
            return false
        }
        if (old is SearchItem.ResultHeader) {
            return old.count == (new as SearchItem.ResultHeader).count
        }
        if (old is SearchItem.RecentHeader) {
            return true
        }
        if (old is SearchItem.RecentRepo) {
            return old.repoId == (new as SearchItem.RecentRepo).repoId
        }
        if (old is SearchItem.SearchRepo) {
            return old.repo == (new as SearchItem.SearchRepo).repo
        }
        return false
    }
}

interface SearchItem {

    companion object {
        const val TYPE_SECTION_RECENT = 0
        const val TYPE_SECTION_SEARCH_RESULT = 1
        const val TYPE_ITEM = 2
    }

    fun viewType(): Int

    class RecentHeader: SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_RECENT
    }

    data class ResultHeader(val count: Int): SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_SEARCH_RESULT
    }

    data class RecentRepo(val repoId: Long): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM
    }

    data class SearchRepo(val repo: RepoEntity): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM
    }
}

