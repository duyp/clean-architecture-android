package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class LogoutUser @Inject constructor(
        private val mUserRepository: UserRepository,
        private val mSettingRepository: SettingRepository,
        private val mAuthenticationRepository: AuthenticationRepository
) {

    /**
     * Logout current user
     */
    fun logoutCurrentUser(): Completable =
            Maybe.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMapCompletable { username ->
                        mUserRepository.logout(username)
                                .onErrorComplete()
                                .doOnComplete {
                                    mAuthenticationRepository.logout(username)
                                    mSettingRepository.setCurrentUsername(null)
                                }
                    }
}