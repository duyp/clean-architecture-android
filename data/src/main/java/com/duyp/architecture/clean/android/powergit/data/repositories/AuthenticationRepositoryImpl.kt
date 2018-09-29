package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.utils.AccountManagerHelper
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
        private val mAccountManagerHelper: AccountManagerHelper
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
}