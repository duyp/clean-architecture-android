package com.duyp.architecture.clean.android.powergit.domain.repositories

/**
 * Interactor which interacts with Android system or Android SDK
 */
interface AndroidInteractor {

    /**
     * Get basic authentication token of given username and password.
     * Note: the exist of this method is due to Android SDK supports. It provide util class to generate Basic token
     * whereas we don't have a Java lib supporting yet.
     */
    fun getBasicAuth(username: String, password: String): String
}