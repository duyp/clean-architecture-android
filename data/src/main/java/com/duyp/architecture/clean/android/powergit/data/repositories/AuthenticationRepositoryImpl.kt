package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.utils.AccountManagerHelper
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import io.reactivex.Maybe
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
        private val mAccountManagerHelper: AccountManagerHelper
) : AuthenticationRepository {

    override fun getAuthentication(username: String): Maybe<String> {
        return Maybe.fromCallable { mAccountManagerHelper.getAuth(username) }
    }

    override fun addOrUpdateUser(username: String, password: String, auth: String) {
        mAccountManagerHelper.saveAccount(username, password, auth)
    }

    override fun logout(username: String) {
        mAccountManagerHelper.logoutAccount(username)
    }
}