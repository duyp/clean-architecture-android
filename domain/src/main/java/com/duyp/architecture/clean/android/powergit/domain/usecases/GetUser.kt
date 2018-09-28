package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class GetUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mCheckUser: CheckUser,
        private val mUserRepository: UserRepository
) {

    /**
     * Get current logged in username
     *
     * @return Maybe emitting username of current logged in user, complete if no logged in user
     */
    fun getCurrentLoggedInUsername() : Maybe<String> =
            Maybe.fromCallable { mAuthenticationRepository.getCurrentUsername() }
                    .flatMap { username ->
                        mCheckUser.isLoggedIn(username)
                                .toMaybe()
                                .flatMap { if (it) Maybe.just(username) else Maybe.empty() }
                    }

    /**
     * @return last logged in username
     */
    fun getLastLoggedInUsername() = mUserRepository.getLastLoggedInUsername()
}