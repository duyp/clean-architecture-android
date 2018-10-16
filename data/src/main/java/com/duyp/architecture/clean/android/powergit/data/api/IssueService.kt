package com.duyp.architecture.clean.android.powergit.data.api

import com.duyp.architecture.clean.android.powergit.data.api.annotations.ForceCache
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueApiData
import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IssueService {

    /**
     * This is the same with [SearchService.searchIssues] but annotated with [ForceCache]
     */
    @GET("search/issues")
    @ForceCache
    fun getIssues(@Query(value = "q", encoded = true) query: String, @Query("page") page: Int):
            Single<PageableApiData<IssueApiData>>
}