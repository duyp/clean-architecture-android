package com.duyp.architecture.clean.android.powergit.domain.entities

data class ListEntity<T> (
        val first: Int = 0,
        val next: Int = 0,
        val prev: Int = 0,
        val last: Int = 0,
        val totalCount: Int = 0,
        val incompleteResults: Boolean = false,
        val items: List<T>,
        val isApiData: Boolean
) {

    fun canLoadMore() = next in 1..last
}