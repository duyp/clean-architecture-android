package com.duyp.architecture.clean.android.powergit.data.repositories

import android.content.SharedPreferences
import com.duyp.architecture.clean.android.powergit.data.SharedPreferenceConstants.KEY_CURRENT_USERNAME
import com.duyp.architecture.clean.android.powergit.data.SharedPreferenceConstants.KEY_LAST_LOGGED_IN_USER
import com.duyp.architecture.clean.android.powergit.data.database.UserDao
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.User
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository
import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val mUserDao: UserDao,
        private val mAuthenticationRepository: AuthenticationRepository,
        private val mSharedPreferences: SharedPreferences
) : UserRepository {

    private val mUserApiToLocalMapper = UserApiToLocalMapper()

    private val mUserLocalToEntityMapper = UserLocalToEntityMapper()

    override fun login(username: String, password: String): Single<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(username: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(username: String): Flowable<User> {
        return mUserDao.getUser(username)
                .map { mUserLocalToEntityMapper.mapFrom(it) }
    }

    override fun getLastLoggedInUsername(): String? {
        return mSharedPreferences.getString(KEY_LAST_LOGGED_IN_USER, null)
    }

    override fun setLastLoggedInUsername(username: String?) {
        if (username != null) {
            mSharedPreferences.edit().putString(KEY_LAST_LOGGED_IN_USER, username).apply()
        } else {
            mSharedPreferences.edit().remove(KEY_LAST_LOGGED_IN_USER).apply()
        }
    }

    override fun getCurrentUsername(): String? {
        return mSharedPreferences.getString(KEY_CURRENT_USERNAME, null)
    }

    override fun setCurrentUsername(username: String?) {
        if (username != null) {
            mSharedPreferences.edit().putString(KEY_CURRENT_USERNAME, username).apply()
        } else {
            mSharedPreferences.edit().remove(KEY_CURRENT_USERNAME).apply()
        }
    }

    override fun isLoggedIn(username: String): Single<Boolean> {
        return mAuthenticationRepository.getAuthentication(username)
                .toSingle("")
                .map { !CommonUtil.isEmpty(it) }
    }

}