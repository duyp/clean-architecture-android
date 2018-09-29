package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class LogoutUser @Inject constructor(
        private val mUserRepository: UserRepository,
        private val mSettingRepository: SettingRepository
) {

    /**
     * Logout current user
     */
    fun logoutCurrentUser(): Completable =
            Maybe.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMapCompletable {
                        mUserRepository.logout(it)
                                .onErrorComplete()
                                .doOnComplete { mSettingRepository.setCurrentUsername(null) }
                    }
}