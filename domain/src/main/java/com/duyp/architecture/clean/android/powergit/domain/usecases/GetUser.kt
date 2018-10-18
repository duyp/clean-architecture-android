package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.Optional
import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.repositories.SettingRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetUser @Inject constructor(
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
     * Get an [Optional] of [UserEntity] by given user id
     */
    fun getUser(id: Long): Single<Optional<UserEntity>> =
            mUserRepository.getUserById(id)
                    .map { Optional.of(it) }
                    .toSingle(Optional.empty())
                    .onErrorReturnItem(Optional.empty())
}