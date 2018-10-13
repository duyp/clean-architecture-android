package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.Optional
import com.duyp.architecture.clean.android.powergit.domain.repositories.RepoRepository
import javax.inject.Inject

class GetRepo @Inject constructor(
        private val mRepoRepository: RepoRepository
){

    /**
     * Get an [Optional] version of a repo by given repo id.
     *
     * @return [Optional.empty] if repo not found or any error occurs
     */
    fun get(repoId: Long) = mRepoRepository.getById(repoId)
            .map { Optional.ofNullable(it) }
            .defaultIfEmpty(Optional.empty())
            .onErrorReturnItem(Optional.empty())!!
}