package com.duyp.architecture.clean.android.powergit.data.entities.release

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.ReleaseAssetEntity

class ReleaseAssetApiToEntityMapper: Mapper<ReleaseAssetApiData, ReleaseAssetEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    override fun mapFrom(e: ReleaseAssetApiData): ReleaseAssetEntity {
        val entity = ReleaseAssetEntity()
        entity.id = e.id
        entity.url = e.url
        entity.browserDownloadUrl = e.browserDownloadUrl
        entity.name = e.name
        entity.label = e.label
        entity.state = e.state
        entity.contentType = e.contentType
        entity.size = e.size
        entity.downloadCount = e.downloadCount
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.uploader = e.uploader?.let { mUserApiToEntityMapper.mapFrom(it) }
        return entity
    }
}