package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RecentRepoLocalData
import io.reactivex.Single

@Dao
abstract class RecentRepoDao: BaseDao<RecentRepoLocalData>() {

    @Query("SELECT repoId FROM RecentRepository INNER JOIN Repository ON RecentRepository.repoId = Repository.id WHERE Repository.fullName LIKE :searchTerm ORDER BY RecentRepository.date DESC")
    abstract fun getRecentRepos(searchTerm: String): Single<List<Long>>
}