package com.duyp.architecture.clean.android.powergit.data.api

import com.duyp.architecture.clean.android.powergit.data.api.annotations.Authenticated
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

    //@GET("users/{username}/received_events")
    //Single<Pageable<Event>> getReceivedEvents(@NonNull @Path("username") String userName, @Query("page") int page);
    //
    //@GET("users/{username}/events")
    //Single<Pageable<Event>> getUserEvents(@NonNull @Path("username") String userName, @Query("page") int page);
    //
    //@GET("users/{username}/repos")
    //Single<Pageable<Repo>> getRepos(@Path("username") @NonNull String username, @QueryMap(encoded = true) Map<String, String> filterParams,
    //                                @Query("page") int page);
    //
    //@GET("user/repos")
    //Single<Pageable<Repo>> getRepos(@QueryMap(encoded = true) Map<String, String> filterParams, @Query(value = "page") int page);
    //
    //@GET("users/{username}/starred")
    //Single<Pageable<Repo>>
    //getStarred(@Path("username") @NonNull String username, @Query("page") int page);
    //
    //@GET("users/{username}/starred?per_page=1")
    //Single<Pageable<Repo>>
    //getStarredCount(@Path("username") @NonNull String username);
    //
    //@GET("users/{username}/following")
    //Single<Pageable<User>> getFollowing(@Path("username") @NonNull String username, @Query("page") int page);
    //
    //@GET("users/{username}/followers")
    //Single<Pageable<User>> getFollowers(@Path("username") @NonNull String username, @Query("page") int page);

    @GET("user/following/{username}")
    fun getFollowStatus(@Path("username") username: String): Single<Response<Boolean>>

    @PUT("user/following/{username}")
    fun followUser(@Path("username") username: String): Single<Response<Boolean>>

    @DELETE("user/following/{username}")
    fun unfollowUser(@Path("username") username: String): Single<Response<Boolean>>

    @GET
    fun getContributions(@Url url: String): Single<String>
}
