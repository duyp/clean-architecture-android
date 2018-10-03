package com.duyp.architecture.clean.android.powergit.data.api

import com.duyp.architecture.clean.android.powergit.data.api.annotations.Authenticated
import com.duyp.architecture.clean.android.powergit.data.entities.event.EventApiData
import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("user")
    fun login(@Header("Authorization") basicToken: String): Single<UserApiData>

    @GET("users/{username}")
    @Authenticated
    fun getUser(@Path("username") username: String): Single<UserApiData>

    @GET("users/{username}/received_events")
    fun getReceivedEvents(@Path("username") userName: String, @Query("page") page: Int):
            Single<PageableApiData<EventApiData>>

    @GET("users/{username}/events")
    fun getUserEvents(@Path("username") userName: String, @Query("page") page: Int):
            Single<PageableApiData<EventApiData>>

    @GET("users/{username}/repos")
    fun getRepos(@Path("username") username: String, @QueryMap(encoded = true) filterParams: Map<String, String>?,
                 @Query("page") page: Int):
            Single<PageableApiData<RepoApiData>>

    @GET("user/repos")
    @Authenticated
    fun getMyRepos(@QueryMap(encoded = true) filterParams: Map<String, String>, @Query(value = "page") page: Int):
            Single<PageableApiData<RepoApiData>>

    //
    //@GET("users/{username}/starred")
    //Single<PageableApiData<Repo>>
    //getStarred(@Path("username") @NonNull String username, @Query("page") int page);
    //
    //@GET("users/{username}/starred?per_page=1")
    //Single<PageableApiData<Repo>>
    //getStarredCount(@Path("username") @NonNull String username);
    //
    //@GET("users/{username}/following")
    //Single<PageableApiData<User>> getFollowing(@Path("username") @NonNull String username, @Query("page") int page);
    //
    //@GET("users/{username}/followers")
    //Single<PageableApiData<User>> getFollowers(@Path("username") @NonNull String username, @Query("page") int page);

    @GET("user/following/{username}")
    fun getFollowStatus(@Path("username") username: String): Single<Response<Boolean>>

    @PUT("user/following/{username}")
    fun followUser(@Path("username") username: String): Single<Response<Boolean>>

    @DELETE("user/following/{username}")
    fun unfollowUser(@Path("username") username: String): Single<Response<Boolean>>

    @GET
    fun getContributions(@Url url: String): Single<String>
}
