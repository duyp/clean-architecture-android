package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.duyp.architecture.clean.android.powergit.data.entities.converters.DateConverter
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData

@Database(version = 1, exportSchema = true, entities = [
    UserLocalData::class,
    RepoLocalData::class
])
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    abstract fun repoDao(): RepoDao

}