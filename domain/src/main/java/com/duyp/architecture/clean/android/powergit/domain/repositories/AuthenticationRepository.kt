package com.duyp.architecture.clean.android.powergit.domain.repositories

import io.reactivex.Maybe

interface AuthenticationRepository {

    /**
     * Get authentication token of given username
     */
    fun getAuthentication(username: String): Maybe<String>
}
