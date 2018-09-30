package com.duyp.architecture.clean.android.powergit.domain.entities

abstract class ListEntity<T: Any> (
        val first: Int = 0,
        val next: Int = 0,
        val prev: Int = 0,
        val last: Int = 0,
        val totalCount: Int = 0,
        val incompleteResults: Boolean = false
)