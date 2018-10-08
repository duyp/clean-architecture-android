package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.database.UserDao
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val mUserService: UserService,
        private val mUserDao: UserDao
) : UserRepository {

    private val mUserApiToLocalMapper = UserApiToLocalMapper()

    private val mUserLocalToEntityMapper = UserLocalToEntityMapper()

    override fun login(token: String): Single<UserEntity> {
        return mUserService.login(token)
                .map { mUserApiToLocalMapper.mapFrom(it) }
                .doOnSuccess {
                    mUserDao.insert(it)
                }
                .map { mUserLocalToEntityMapper.mapFrom(it) }
    }

    override fun logout(username: String): Completable {
        return Completable.complete() // don't have logout api supported yet
    }

    override fun getUser(username: String): Flowable<UserEntity> {
        return mUserDao.getUser(username)
                .map { mUserLocalToEntityMapper.mapFrom(it) }
    }

}