package com.duyp.architecture.clean.android.powergit.domain.entities

data class ListEntity<T> (

        val first: Int = 0,

        val next: Int = 0,

        val prev: Int = 0,

        val last: Int = 0,

        val totalCount: Int = 0,

        val incompleteResults: Boolean = false,

        val items: List<T> = emptyList(),

        val isOfflineData: Boolean = false,

        val apiError: Throwable? = null
) {

    fun canLoadMore() = next in 1..last

    fun nextIsFirstPage() = next == STARTING_PAGE

    companion object {
        const val STARTING_PAGE = 0
    }
}