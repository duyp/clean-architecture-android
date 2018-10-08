package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AndroidInteractor
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoginUser @Inject constructor(
        private val mUserRepository: UserRepository,
        private val mSettingRepository: SettingRepository,
        private val mAndroidInteractor: AndroidInteractor,
        private val mAuthenticationRepository: AuthenticationRepository
) {

    /**
     * Login an user with given username and password. If successfully, the user will be stored in Android Account
     * Manager and also be selected as current user
     */
    fun login(username: String, password: String): Completable {
        return Single.fromCallable { mAndroidInteractor.getBasicAuth(username, password) }
                .flatMapCompletable { token ->
                    mUserRepository.login(token)
                            .doOnSuccess {
                                mSettingRepository.setLastLoggedInUsername(username)
                                mSettingRepository.setCurrentUsername(username)
                                mAuthenticationRepository.addOrUpdateUser(username, password, token)
                            }
                            .ignoreElement()
                }
    }
}