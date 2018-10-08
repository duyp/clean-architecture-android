package com.duyp.architecture.clean.android.powergit.data.entities.pagination

import com.google.gson.annotations.SerializedName

/**
 * Wrap a list of data which is pageable
 * See [com.duyp.architecture.clean.android.powergit.data.api.interceptors.PaginationInterceptor]
 */
data class PageableApiData<M> (

        @SerializedName("first")
        var first: Int? = null,

        @SerializedName("next")
        var next: Int? = null,

        @SerializedName("prev")
        var prev: Int? = null,

        @SerializedName("last")
        var last: Int? = null,

        @SerializedName("totalCount")
        var totalCount: Int = 0,

        @SerializedName("incompleteResults")
        var incompleteResults: Boolean = false,

        @SerializedName("items")
        var items: List<M>
)