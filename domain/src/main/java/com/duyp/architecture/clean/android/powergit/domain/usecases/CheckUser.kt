package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mSettingRepository: SettingRepository
) {

    /**
     * Check if an user is logged in
     *
     * @return Single emitting true if given user is logged in, otherwise false regardless error
     */
    fun isLoggedIn(username: String): Single<Boolean> =
            Single.fromCallable { mAuthenticationRepository.getAuthentication(username) }
                    .map { it.isNotEmpty() }
                    .onErrorReturnItem(false)

    /**
     * Check if has current user which is logged in
     *
     * @return Single emitting true if has logged in user, otherwise false regardless error
     */
    fun hasLoggedInUser(): Single<Boolean> =
            Single.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMap { isLoggedIn(it) }
                    .onErrorReturnItem(false)

}