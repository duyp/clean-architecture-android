package com.duyp.architecture.clean.android.powergit.domain.repositories

interface SettingRepository {

    /**
     * Get current user name
     *
     * @return current username, null if no user
     */
    fun getCurrentUsername() : String?

    /**
     * Set last logged in username
     */
    fun setCurrentUsername(username: String?)

    /**
     * Get last logged in username
     *
     * @return last logged in username, null if no user logged in yet
     */
    fun getLastLoggedInUsername() : String?

    /**
     * Set last logged in username
     */
    fun setLastLoggedInUsername(username: String?)
}
