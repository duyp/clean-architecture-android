package com.duyp.architecture.clean.android.powergit.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface RecentRepoRepository {

    /**
     * Save a repo as recent viewed
     */
    fun saveRecentRepo(id: Long, date: Date): Completable

    /**
     * Get list of recent viewed repository ids with given search terms
     */
    fun getRecentRepoIds(searchTerm: String): Single<List<Long>>
}