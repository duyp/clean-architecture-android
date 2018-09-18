package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Maybe

class GetUser(private val mUserRepository: UserRepository) {

    fun getCurrentLoggedInUsername() = mUserRepository.getLastLoggedInUsername()
            .flatMap { username ->
                mUserRepository.isLoggedIn(username)
                        .flatMapMaybe {
                            if (it) Maybe.just(username) else Maybe.empty()
                        }
            }

    fun getCurrentLoggedInUser() = getCurrentLoggedInUsername()
            .flatMapPublisher { mUserRepository.getUser(it) }
}