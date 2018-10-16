package com.duyp.architecture.clean.android.powergit.data.entities.label

import com.duyp.architecture.clean.android.powergit.domain.entities.LabelEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class LabelLocalToEntityMapper: Mapper<LabelLocalData, LabelEntity>() {

    override fun mapFrom(e: LabelLocalData): LabelEntity {
        val entity = LabelEntity()
        entity.id = e.id
        entity.url = e.url
        entity.name = e.name
        entity.color = e.color
        entity._default = e._default
        return entity
    }
}