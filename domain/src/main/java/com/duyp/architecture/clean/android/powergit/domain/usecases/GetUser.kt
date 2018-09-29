package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class GetUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mCheckUser: CheckUser,
        private val mSettingRepository: SettingRepository
) {

    /**
     * Get current logged in username
     *
     * @return Maybe emitting username of current logged in user, complete if no logged in user
     */
    fun getCurrentLoggedInUsername(): Maybe<String> =
            Maybe.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMap { username ->
                        mCheckUser.isLoggedIn(username)
                                .toMaybe()
                                .flatMap { if (it) Maybe.just(username) else Maybe.empty() }
                    }

    /**
     * @return last logged in username
     */
    fun getLastLoggedInUsername() = mSettingRepository.getLastLoggedInUsername()

    /**
     * Get all users in account manager which have authentication saved
     * @return all username
     */
    fun getAuthenticatedUsersInAccountManager(): Single<List<String>> =
            mAuthenticationRepository.getAllAccounts()
                    .flattenAsObservable { it }
                    // only get authenticated users (have token saved)
                    .filter { !CommonUtil.isEmpty(it, mAuthenticationRepository.getAuthentication(it)) }
                    .toList()
                    .onErrorReturnItem(emptyList())
}