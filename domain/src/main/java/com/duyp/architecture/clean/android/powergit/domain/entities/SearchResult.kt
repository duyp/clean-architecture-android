package com.duyp.architecture.clean.android.powergit.domain.entities

data class SearchResult<T>(
        val apiList: ListEntity<T>? = null,
        val localList: ListEntity<T>? = null
)