package com.duyp.architecture.clean.android.powergit.data.entities.commit

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.CommitEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class CommitApiToEntityMapper: Mapper<CommitApiData, CommitEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mGitCommitApiToEntityMapper = GitCommitApiToEntityMapper()

    override fun mapFrom(e: CommitApiData): CommitEntity {
        val entity = CommitEntity()
        entity.sha = e.sha
        entity.url = e.url
        entity.message = e.message
        entity.ref = e.ref
        entity.commentCount = e.commentCount
        entity.distincted = e.distincted
        entity.author = e.author?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.committer = e.committer?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.tree = e.tree?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.parents = e.parents?.let { this.mapFrom(it) }
        entity.gitCommit = e.gitCommit?.let { mGitCommitApiToEntityMapper.mapFrom(it) }
        return entity
    }
}