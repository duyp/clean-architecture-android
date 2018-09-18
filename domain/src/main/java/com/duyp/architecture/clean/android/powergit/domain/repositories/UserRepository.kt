package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface UserRepository {

    /**
     * Login an user by given username and password. After logging in successfully, the user will be stored in
     * Android Account Manager
     *
     * @return Single which emits user entity if successfully
     */
    fun login(username: String, password: String): Single<User>

    /**
     * Logout an user
     */
    fun logout(username: String): Completable

    /**
     * Get an user entity of given username
     *
     * @return Flowable emitting user entity whenever user's data changed
     */
    fun getUser(username: String) : Flowable<User>

    /**
     * Get last logged in username
     *
     * @return Maybe which emits username if has last logged in user, otherwise complete
     */
    fun getLastLoggedInUsername() : Maybe<String>

    /**
     * Check if an user is logged in
     *
     * @return Single emitting true if given user is logged in, otherwise false
     */
    fun isLoggedIn(username: String) : Single<Boolean>
}