package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.support.v7.util.DiffUtil
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.issue.GetIssue
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GetRepo
import io.reactivex.schedulers.Schedulers

/**
 * Represent a item to be displayed in search adapter
 */
interface SearchItem {

    fun viewType(): Int

    data class RecentHeader(
            val repoCount: Int = 0,
            val issueCount: Int = 0,
            val userCount: Int = 0,
            val currentTab: Int,
            val currentSearchTerm: String
    ): SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_RECENT
    }

    data class ResultHeader(
            val totalCount: Long,
            val loading: Boolean,
            val currentSearchTerm: String,
            val errorMessage: String? = null
    ): SearchItem {

        override fun viewType() = SearchItem.TYPE_SECTION_SEARCH_RESULT
    }

    data class RecentRepo(internal val repoId: Long, private val mGetRepo: GetRepo): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_RECENT_REPO

        fun getRepo(): RepoEntity? = mGetRepo.get(repoId)
                .subscribeOn(Schedulers.io())
                .blockingGet()
                .orElse(null)
    }
    
    data class RecentIssue(internal val issueId: Long, private val mGetIssue: GetIssue): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_RECENT_ISSUE

        fun getIssue(): IssueEntity? = mGetIssue.get(issueId)
                .subscribeOn(Schedulers.io())
                .blockingGet()
                .orElse(null)
    }

    data class RecentUser(internal val userId: Long, private val mGetUser: GetUser): SearchItem {

        override fun viewType() = SearchItem.TYPE_ITEM_RECENT_USER

        fun getUser(): UserEntity? = mGetUser.getUser(userId)
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
        const val TYPE_ITEM_RECENT_REPO = 2
        const val TYPE_ITEM_RECENT_ISSUE = 4
        const val TYPE_ITEM_RECENT_USER = 5
        const val TYPE_ITEM_SEARCH_RESULT = 3
    }
}

internal object SearchDiffUtils {

    internal fun calculateDiffResult(oldList: ListEntity<SearchItem>, newList: ListEntity<SearchItem>):
            DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize() = oldList.items.size

            override fun getNewListSize() = newList.items.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemEquals(oldList.items[oldItemPosition], newList.items[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areContentsEquals(oldList.items[oldItemPosition], newList.items[newItemPosition])
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return getChangePayload(oldList.items[oldItemPosition], newList.items[newItemPosition])
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
        if (old is SearchItem.RecentHeader) {
            return old.repoCount == (new as SearchItem.RecentHeader).repoCount && old.currentTab == new.currentTab
                    && old.issueCount == new.issueCount && old.userCount == new.userCount
                    && old.currentSearchTerm == new.currentSearchTerm
        }
        if (old is SearchItem.ResultHeader) {
            return old.totalCount == (new as SearchItem.ResultHeader).totalCount
                    && old.loading == new.loading && old.errorMessage == new.errorMessage
        }
        if (old is SearchItem.RecentRepo) {
            return old.repoId == (new as SearchItem.RecentRepo).repoId
        }
        if (old is SearchItem.SearchResultRepo) {
            return old.repo == (new as SearchItem.SearchResultRepo).repo
        }
        return false
    }

    private fun getChangePayload(old: SearchItem, new: SearchItem): Any? {
        if (old is SearchItem.RecentHeader && new is SearchItem.RecentHeader) {
            return old // use old item to decide custom animation
        }
        return null
    }
}