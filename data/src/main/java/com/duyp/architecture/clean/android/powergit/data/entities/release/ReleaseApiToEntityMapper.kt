package com.duyp.architecture.clean.android.powergit.data.entities.release

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.ReleaseEntity

class ReleaseApiToEntityMapper: Mapper<ReleaseApiData, ReleaseEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mReleaseAssetApiToEntityMapper = ReleaseAssetApiToEntityMapper()

    override fun mapFrom(e: ReleaseApiData): ReleaseEntity {
        val entity = ReleaseEntity()
        entity.id = e.id
        entity.url = e.url
        entity.htmlUrl = e.htmlUrl
        entity.assetsUrl = e.assetsUrl
        entity.uploadUrl = e.uploadUrl
        entity.tagName = e.tagName
        entity.targetCommitish = e.targetCommitish
        entity.name = e.name
        entity.repoId = e.repoId
        entity.login = e.login
        entity.tarballUrl = e.tarballUrl
        entity.body = e.body
        entity.zipBallUrl = e.zipBallUrl
        entity.draft = e.draft
        entity.preRelease = e.preRelease
        entity.createdAt = e.createdAt
        entity.publishedAt = e.publishedAt
        entity.author = e.author?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.assets = e.assets?.let { mReleaseAssetApiToEntityMapper.mapFrom(it) }
        return entity
    }
}