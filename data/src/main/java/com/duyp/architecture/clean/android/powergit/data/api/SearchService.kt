package com.duyp.architecture.clean.android.powergit.data.api

import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueApiData
import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search/repositories")
    fun searchRepositories(@Query(value = "q", encoded = true) query: String, @Query("page") page: Int):
            Single<PageableApiData<RepoApiData>>

    @GET("search/issues")
    fun searchIssues(@Query(value = "q", encoded = true) query: String, @Query("page") page: Int):
            Single<PageableApiData<IssueApiData>>

    @GET("search/users")
    fun searchUsers(@Query(value = "q", encoded = true) query: String, @Query("page") page: Int):
            Single<PageableApiData<UserApiData>>
}