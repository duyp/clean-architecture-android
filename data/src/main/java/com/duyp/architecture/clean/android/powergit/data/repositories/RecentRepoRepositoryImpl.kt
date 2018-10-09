package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.database.RecentRepoDao
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RecentRepoLocalData
import com.duyp.architecture.clean.android.powergit.domain.repositories.RecentRepoRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class RecentRepoRepositoryImpl @Inject constructor(
        private val mRecentRepoDao: RecentRepoDao
): RecentRepoRepository {

    override fun saveRecentRepo(id: Long, date: Date): Completable {
        return Completable.fromAction { mRecentRepoDao.insert(RecentRepoLocalData(repoId = id, date = date)) }
    }

    override fun getRecentRepoIds(searchTerm: String): Single<List<Long>> {
        return mRecentRepoDao.getRecentRepos("%$searchTerm%")
    }
}