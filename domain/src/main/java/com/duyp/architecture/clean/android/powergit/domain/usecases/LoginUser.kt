package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import javax.inject.Inject

class LoginUser @Inject constructor(private val mUserRepository: UserRepository) {

    /**
     * Login an user with given username and password. If successfully, the user will be selected as current user
     */
    fun login(username: String, password: String): Completable =
            mUserRepository.login(username, password)
                    .doOnSuccess {
                        mUserRepository.setLastLoggedInUsername(it.login)
                        mUserRepository.setCurrentUsername(it.login)
                    }
                    .ignoreElement()
}