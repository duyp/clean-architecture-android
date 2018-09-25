package com.duyp.architecture.clean.android.powergit.domain.repositories

import io.reactivex.Maybe

interface AuthenticationRepository {

    /**
     * Get authentication token of given username
     */
    fun getAuthentication(username: String): Maybe<String>

    /**
     * Add or update an user
     */
    fun addOrUpdateUser(username: String, password: String, auth: String)

    /**
     * Logout specific user by given username
     */
    fun logout(username: String)
}
