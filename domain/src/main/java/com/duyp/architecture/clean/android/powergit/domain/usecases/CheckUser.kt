package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class CheckUser @Inject constructor(private val mAuthenticationRepository: AuthenticationRepository) {

    /**
     * Check if an user is logged in
     *
     * @return Single emitting true if given user is logged in, otherwise false
     */
    fun isLoggedIn(username: String): Single<Boolean> =
            Single.fromCallable { !CommonUtil.isEmpty(mAuthenticationRepository.getAuthentication(username)) }
                    .onErrorReturnItem(false)

    fun hasLoggedInUser(): Single<Boolean> =
            Maybe.fromCallable { mAuthenticationRepository.getCurrentUsername() }
                    .toSingle("")
                    .flatMap {
                        if (it.isEmpty()) Single.just(false) else isLoggedIn(it)
                    }
                    .onErrorReturnItem(false)

}