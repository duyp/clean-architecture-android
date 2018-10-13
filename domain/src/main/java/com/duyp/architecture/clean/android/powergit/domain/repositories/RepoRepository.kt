package com.duyp.architecture.clean.android.powergit.domain.repositories

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import io.reactivex.Maybe
import io.reactivex.Single

interface RepoRepository {

    /**
     * Get pubic repo list (ids) of an user with given filter option and page. If network problem occurs when
     * attempting to load the first page, offline data (cache in database) will be returned
     *
     * @param username user
     * @param filterOptions option for filtering and sorting
     * @param page page number to load
     */
    fun getUserRepoList(username: String, filterOptions: FilterOptions, page: Int):
            Single<ListEntity<Long>>

    /**
     * Get repo list (ids) of current user (both public and private since we have credentials) with given filter option
     * and page. If network problem occurs when attempting to load the first page, offline data (cache in database)
     * will be returned
     *
     * @param username user
     * @param filterOptions option for filtering and sorting
     * @param page page number to load
     */
    fun getMyUserRepoList(username: String, filterOptions: FilterOptions, page: Int): Single<ListEntity<Long>>

    /**
     * Get a repo by given id
     *
     * @return [Maybe.empty] if no repo found
     */
    fun getById(id: Long): Maybe<RepoEntity>
}