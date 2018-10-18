package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface UserRepository {

    /**
     * Login an user by given authentication token.
     *
     * @return Single which emits user entity if successfully
     */
    fun login(token: String): Single<UserEntity>

    /**
     * Logout an user
     */
    fun logout(username: String): Completable

    /**
     * Get an user entity of given username
     *
     * @return Flowable emitting user entity whenever user's data changed
     */
    fun getUser(username: String) : Flowable<UserEntity>

    /**
     * Get [Maybe] of an user by given [id]
     *
     * @return complete if no user
     */
    fun getUserById(id: Long): Maybe<UserEntity>
}