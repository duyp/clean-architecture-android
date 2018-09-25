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
}