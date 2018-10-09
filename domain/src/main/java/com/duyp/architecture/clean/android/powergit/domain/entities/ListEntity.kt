package com.duyp.architecture.clean.android.powergit.domain.entities

import io.reactivex.Single

/**
 * Entity contains list data as well as pagination info of an Entity [T]
 */
data class ListEntity<T> (

        private val first: Int? = null,

        private val next: Int? = null,

        private val prev: Int? = null,

        private val last: Int? = null,

        val totalCount: Int = 0,

        val incompleteResults: Boolean = false,

        val items: List<T> = emptyList(),

        val isOfflineData: Boolean = false,

        val apiError: Throwable? = null
) {

    /**
     * @return true if we can load more for this list
     */
    fun canLoadMore() = next in getFirstPage()..getLastPage()

    /**
     * Merge with previous page. We don't merge if previous page is empty or this page is first page (eg. came from
     * refresh)
     *
     * @return new immutable list entity which contains all items of previous list and current list, only if
     * previous list has page number < current list.
     */
    fun mergeWith(previousList: ListEntity<T>): ListEntity<T> {
        if (this.isFirstPage() || previousList.items.isEmpty()) {
            return this
        }
        return if (this.isLastPage() || previousList.getNextPage() < this.getNextPage()) {
            copy(items = previousList.items + this.items)
        } else {
            this
        }
    }

    /**
     * @return true if this page is last page
     */
    private fun isLastPage() = next == null && prev != null

    /**
     * @return true if this page is first page
     */
    private fun isFirstPage() = next == getFirstPage() + 1

    /**
     * Get the first page number, uses [STARTING_PAGE] if [first] is not set
     */
    internal fun getFirstPage() = first ?: STARTING_PAGE

    internal fun getLastPage() = last ?: 0

    fun getNextPage() = next ?: STARTING_PAGE

    companion object {
        const val STARTING_PAGE = 1
    }
}

fun <T> Single<ListEntity<T>>.mergeWithPreviousPage(previousList: ListEntity<T>): Single<ListEntity<T>> {
    return map { it.mergeWith(previousList) }
}
