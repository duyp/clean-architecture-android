package com.duyp.architecture.clean.android.powergit.data.repositories

import android.content.SharedPreferences
import com.duyp.architecture.clean.android.powergit.data.SharedPreferenceConstants
import com.duyp.architecture.clean.android.powergit.data.utils.putOrRemoveString
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
        private val mSharedPreferences: SharedPreferences
) : SettingRepository {

    override fun getCurrentUsername(): String? {
        return mSharedPreferences.getString(SharedPreferenceConstants.KEY_CURRENT_USERNAME, null)
    }

    override fun setCurrentUsername(username: String?) {
        mSharedPreferences.putOrRemoveString(SharedPreferenceConstants.KEY_CURRENT_USERNAME, username)
    }

    override fun getLastLoggedInUsername(): String? {
        return mSharedPreferences.getString(SharedPreferenceConstants.KEY_LAST_LOGGED_IN_USER, null)
    }

    override fun setLastLoggedInUsername(username: String?) {
        mSharedPreferences.putOrRemoveString(SharedPreferenceConstants.KEY_LAST_LOGGED_IN_USER, username)
    }
}