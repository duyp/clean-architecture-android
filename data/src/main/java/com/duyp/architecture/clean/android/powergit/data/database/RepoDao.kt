package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoLocalData
import io.reactivex.Single

@Dao
abstract class RepoDao: BaseDao<RepoLocalData>() {

    @Query("SELECT * FROM Repository WHERE owner_login = :username")
    abstract fun getUserRepos(username: String): Single<List<RepoLocalData>>

    @Query("SELECT COUNT(*) FROM Repository WHERE id = :id")
    abstract fun countRepo(id: Long): Int
}