package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.database.AuthenticationDao
import com.duyp.architecture.clean.android.powergit.data.entities.authentication.AuthenticationLocalData
import com.duyp.architecture.clean.android.powergit.domain.repositories.AuthenticationRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
        private val mAuthenticationDao: AuthenticationDao
) : AuthenticationRepository {

    override fun getAuthentication(username: String): Maybe<String> {
        return mAuthenticationDao.getAuth(username)
    }

    override fun setAuthentication(username: String, auth: String) : Completable {
        return Completable.fromAction {
            mAuthenticationDao.insert(AuthenticationLocalData(username, auth))
        }
    }

    override fun removeAuthentication(username: String): Completable {
        return Completable.fromAction {
            mAuthenticationDao.deleteAuth(username)
        }
    }
}