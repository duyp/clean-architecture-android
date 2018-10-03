package com.duyp.architecture.clean.android.powergit.data.entities.wiki

import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.WikiEntity

class WikiApiToEntityMapper: Mapper<WikiApiData, WikiEntity>() {

    override fun mapFrom(e: WikiApiData): WikiEntity {
        val entity = WikiEntity()
        entity.pageName = e.pageName
        entity.title = e.title
        entity.summary = e.summary
        entity.action = e.action
        entity.sha = e.sha
        entity.htmlUrl = e.htmlUrl
        return entity
    }
}