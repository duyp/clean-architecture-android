package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class LogoutUser @Inject constructor(private val mUserRepository: UserRepository) {

    /**
     * Logout current user
     */
    fun logoutCurrentUser(): Completable =
            Maybe.fromCallable { mUserRepository.getCurrentUsername() }
                    .flatMapCompletable {
                        mUserRepository.logout(it)
                                .onErrorComplete()
                                .doOnComplete { mUserRepository.setCurrentUsername(null) }
                    }
}