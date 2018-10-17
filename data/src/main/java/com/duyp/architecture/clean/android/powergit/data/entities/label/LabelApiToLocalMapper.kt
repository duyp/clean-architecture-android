package com.duyp.architecture.clean.android.powergit.data.entities.label

import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class LabelApiToLocalMapper: Mapper<LabelApiData, LabelLocalData>() {

    override fun mapFrom(e: LabelApiData): LabelLocalData {
        val localData = LabelLocalData()
        localData.id = e.id
        localData.url = e.url
        localData.name = e.name
        localData.color = e.color
        localData._default = e._default
        return localData
    }
}