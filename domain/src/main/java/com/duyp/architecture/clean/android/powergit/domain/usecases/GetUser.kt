package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetUser @Inject constructor(
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mCheckUser: CheckUser,
        private val mSettingRepository: SettingRepository,
        private val mUserRepository: UserRepository
) {

    /**
     * Get current logged in user in a [Flowable] stream which will get updated whenever data changed, but not get
     * updated if user log out / log in.
     *
     * That means this stream can't switch to subscribe on other users once it is
     * subscribed when the first time [Flowable.subscribe] is called. Therefore if we do log in / log out users,
     * we should re-subscribe to have correct stream on current logged in user.
     */
    fun getCurrentLoggedInUser(): Flowable<UserEntity> = getCurrentLoggedInUsername()
            .flatMapPublisher {
                mUserRepository.getUser(it)
            }

    /**
     * Get current logged in username
     *
     * @return Maybe emitting username of current logged in user, complete if no logged in user
     */
    fun getCurrentLoggedInUsername(): Single<String> =
            Single.fromCallable { mSettingRepository.getCurrentUsername() }
                    .flatMap { username ->
                        mCheckUser.isLoggedIn(username)
                                .flatMap {
                                    if (it)
                                        Single.just(username)
                                    else
                                        Single.error(AuthenticationException())
                                }
                    }
                    .onErrorResumeNext(Single.error(AuthenticationException()))

    /**
     * @return last logged in username
     */
    fun getLastLoggedInUsername() = mSettingRepository.getLastLoggedInUsername()

    /**
     * Get all users in account manager which have authentication saved. This is used for checking if we have some
     * users saved in account managers but don't have any indicator in the app about their existing (in case of
     * user cleared app data)
     *
     * @return all username
     */
    fun getAuthenticatedUsersInAccountManager(): Single<List<String>> =
            mAuthenticationRepository.getAllAccounts()
                    .flattenAsObservable { it }
                    // only get authenticated users (have token saved)
                    .filter { !CommonUtil.isEmpty(it, mAuthenticationRepository.getAuthentication(it)) }
                    .toList()
                    .onErrorReturnItem(emptyList())
}