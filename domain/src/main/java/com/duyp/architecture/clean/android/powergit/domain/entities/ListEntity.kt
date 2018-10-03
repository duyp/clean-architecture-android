package com.duyp.architecture.clean.android.powergit.domain.entities

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Entity contains list data as well as pagination info of an Entity [T]
 */
data class ListEntity<T> (

        val first: Int = 0,

        val next: Int = STARTING_PAGE,

        val prev: Int = 0,

        val last: Int = 0,

        val totalCount: Int = 0,

        val incompleteResults: Boolean = false,

        val items: List<T> = emptyList(),

        val isOfflineData: Boolean = false,

        val apiError: Throwable? = null
) {

    /**
     * @return true if we can load more for this list
     */
    fun canLoadMore() = next in 1..last

    /**
     * Merge with previous page. We don't merge if previous page is empty or this page is first page (eg. came from
     * refresh)
     *
     * @return new immutable list entity which contains all items of previous list and current list, only if
     * previous list has page number < current list.
     */
    fun mergeWith(previousList: ListEntity<T>): ListEntity<T> {
        if (isFirstPage() || previousList.items.isEmpty()) {
            return this
        }
        // next == 0 means last page
        return if (this.isLastPage() || previousList.next < this.next) {
            copy(items = previousList.items + this.items)
        } else {
            this
        }
    }

    /**
     * @return true if this page is last page
     */
    private fun isLastPage() = next == 0 && prev > 0

    /**
     * @return true if this page is first page
     */
    private fun isFirstPage() = next == getFirstPage() + 1

    /**
     * Get the first page number, uses [STARTING_PAGE] if [first] is not set
     */
    private fun getFirstPage() = if (first > 0) first else STARTING_PAGE

    companion object {
        const val STARTING_PAGE = 1
    }
}

fun <T> Single<ListEntity<T>>.mergeWithPreviousPage(previousList: ListEntity<T>): Single<ListEntity<T>> {
    return map { it.mergeWith(previousList) }
}

fun <T> Observable<ListEntity<T>>.mergeWithPreviousPage(previousList: ListEntity<T>): Observable<ListEntity<T>> {
    return map { it.mergeWith(previousList) }
}