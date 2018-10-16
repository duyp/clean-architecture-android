package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.duyp.architecture.clean.android.powergit.data.entities.converters.DateConverter
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.label.IssueLabelsLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RecentRepoLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserLocalData

@Database(version = 1, exportSchema = true, entities = [
    UserLocalData::class,
    RepoLocalData::class,
    RecentRepoLocalData::class,
    IssueLocalData::class,
    LabelLocalData::class,
    IssueLabelsLocalData::class
])
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    abstract fun repoDao(): RepoDao

    abstract fun recentRepoDao(): RecentRepoDao

    abstract fun issueDao(): IssueDao

    abstract fun labelDao(): LabelDao
}