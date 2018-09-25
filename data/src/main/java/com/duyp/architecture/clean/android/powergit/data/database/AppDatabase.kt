package com.duyp.architecture.clean.android.powergit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData

@Database(version = 1, exportSchema = true, entities = [
    (UserLocalData::class)
])
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

}