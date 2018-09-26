package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData
import io.reactivex.Flowable

@Dao
abstract class UserDao : BaseDao<UserLocalData>() {

    @Query("SELECT * FROM User WHERE login = :username")
    abstract fun getUser(username: String) : Flowable<UserLocalData>


}