package com.duyp.architecture.clean.android.powergit.data.repositories

import android.content.SharedPreferences
import com.duyp.architecture.clean.android.powergit.data.SharedPreferenceConstants
import com.duyp.architecture.clean.android.powergit.data.utils.AccountManagerHelper
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
        private val mAccountManagerHelper: AccountManagerHelper,
        private val mSharedPreferences: SharedPreferences
) : AuthenticationRepository {

    override fun getAllAccounts(): Single<List<String>> {
        return Maybe.fromCallable { mAccountManagerHelper.getAllAccounts() }
                .map { it.toList() }
                .flattenAsObservable { it }
                .map { it.name }
                .toList()
    }

    override fun getAuthentication(username: String): String? {
        return mAccountManagerHelper.getAuth(username)
    }

    override fun addOrUpdateUser(username: String, password: String, auth: String) {
        mAccountManagerHelper.saveAccount(username, password, auth)
    }

    override fun logout(username: String) {
        mAccountManagerHelper.logoutAccount(username)
    }

    override fun getCurrentUsername(): String? {
        return mSharedPreferences.getString(SharedPreferenceConstants.KEY_CURRENT_USERNAME, null)
    }

    override fun setCurrentUsername(username: String?) {
        if (username != null) {
            mSharedPreferences.edit().putString(SharedPreferenceConstants.KEY_CURRENT_USERNAME, username).apply()
        } else {
            mSharedPreferences.edit().remove(SharedPreferenceConstants.KEY_CURRENT_USERNAME).apply()
        }
    }
}