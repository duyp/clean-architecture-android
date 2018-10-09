package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity

data class RepoSearchResult(
        val recentRepoIds: List<Long>,
        val searchResultList: ListEntity<RepoEntity>
)