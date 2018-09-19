package com.duyp.architecture.clean.android.powergit.domain.repositories

import io.reactivex.Completable
import io.reactivex.Maybe

interface AuthenticationRepository {

    /**
     * Get authentication token of given username
     */
    fun getAuthentication(username: String): Maybe<String>

    /**
     * Set authentication token for given username
     */
    fun setAuthentication(username: String, auth: String) : Completable

    /**
     * Remove user's authentication
     */
    fun removeAuthentication(username: String): Completable
}
