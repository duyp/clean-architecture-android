package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class GetUser @Inject constructor(
        private val mUserRepository: UserRepository,
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mCheckUser: CheckUser
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
}