package com.duyp.architecture.clean.android.powergit.data.entities.commit

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.GitCommitEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class GitCommitApiToEntityMapper: Mapper<GitCommitApiData, GitCommitEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    override fun mapFrom(e: GitCommitApiData): GitCommitEntity {
        val entity = GitCommitEntity()
        entity.sha = e.sha
        entity.url = e.url
        entity.message = e.message
        entity.distincted = e.distincted
        entity.commentCount = e.commentCount
        entity.author = e.author?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.committer = e.committer?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.tree = e.tree?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.parents = e.parents?.let { this.mapFrom(it) }
        return entity
    }
}