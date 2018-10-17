package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CheckUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mSettingRepository: SettingRepository,
        private val mLoginUser: LoginUser
) {

    /**
     * Check if an user is logged in
     *
     * @return Single emitting true if given user is logged in, otherwise false regardless error
     */
    fun isLoggedIn(username: String): Single<Boolean> =
            Single.fromCallable { mAuthenticationRepository.getAuthentication(username) }
                    .map { it.isNotEmpty() }
                    .onErrorReturnItem(false)

    /**
     * Check if has current user which is logged in
     *
     * @return Single emitting true if has logged in user, otherwise false regardless error
     */
    fun hasLoggedInUser(): Single<Boolean> =
            Single.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMap { isLoggedIn(it) }
                    .onErrorReturnItem(false)

    /**
     * Check for current logged in user. If we don't have any current logged user stored in our app, the account
     * manager will be checked instead (the case user clear app data but didn't clear accounts in account settings).
     * In this case if we have any account which has password and authentication, it will be logged in automatically
     * view [LoginUser] to verify the user's password haven't been changed in server side as well as getting user
     * info and store it in our database
     *
     * @return Single emitting true if has logged in user, otherwise false
     */
    fun checkCurrentUser(): Single<Boolean> {
        return hasLoggedInUser()
                .flatMap {
                    if (it) {
                        Single.just(true)
                    } else {
                        mAuthenticationRepository.getAuthenticatedAccounts()
                                .flatMap { accounts ->
                                    if (accounts.isEmpty()) {
                                        Single.just(false)
                                    } else {
                                        Completable.fromAction {
                                            mSettingRepository.setLastLoggedInUsername(accounts[0].username)
                                        }
                                                .andThen(mLoginUser.login(accounts[0].username, accounts[0].password))
                                                .andThen(Single.just(true))
                                                .onErrorReturnItem(false)
                                    }
                                }

                    }
                }
    }

}