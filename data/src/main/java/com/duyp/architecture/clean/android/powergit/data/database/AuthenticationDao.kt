package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.authentication.AuthenticationLocalData
import io.reactivex.Maybe

abstract class AuthenticationDao : BaseDao<AuthenticationLocalData>() {

    @Query("SELECT auth FROM Authentication WHERE username = :username")
    abstract fun getAuth(username: String): Maybe<String>

    @Query("DELETE FROM Authentication WHERE username = :username")
    abstract fun deleteAuth(username: String)
}