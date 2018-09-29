package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class CheckUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mSettingRepository: SettingRepository
) {

    /**
     * Check if an user is logged in
     *
     * @return Single emitting true if given user is logged in, otherwise false
     */
    fun isLoggedIn(username: String): Single<Boolean> =
            Maybe.fromCallable { mAuthenticationRepository.getAuthentication(username) }
                    .toSingle("")
                    .map { it.isNotEmpty() }
                    .onErrorReturnItem(false)

    /**
     * Check if has current user which is logged in
     */
    fun hasLoggedInUser(): Single<Boolean> =
            Maybe.fromCallable { mSettingRepository.getCurrentUsername() }
                    .toSingle("")
                    .flatMap {
                        if (it.isEmpty()) Single.just(false) else isLoggedIn(it)
                    }
                    .onErrorReturnItem(false)

}