package com.duyp.architecture.clean.android.powergit.domain.entities.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity

data class RepoListEntity(
        private val repoIds: List<Int>
): ListEntity<Int>()