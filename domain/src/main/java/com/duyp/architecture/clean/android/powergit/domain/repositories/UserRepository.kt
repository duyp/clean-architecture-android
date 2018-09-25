package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable
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
     * @return last logged in username, null if no user logged in yet
     */
    fun getLastLoggedInUsername() : String?

    /**
     * Set last logged in username
     *
     */
    fun setLastLoggedInUsername(username: String?)

    /**
     * Get current user name
     *
     * @return current username, null if no user
     */
    fun getCurrentUsername() : String?

    /**
     * Set last logged in username
     *
     */
    fun setCurrentUsername(username: String?)
}