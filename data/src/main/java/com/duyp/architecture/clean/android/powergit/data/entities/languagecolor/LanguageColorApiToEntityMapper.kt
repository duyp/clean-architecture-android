package com.duyp.architecture.clean.android.powergit.data.entities.languagecolor

import com.duyp.architecture.clean.android.powergit.domain.entities.LanguageColorEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class LanguageColorApiToEntityMapper: Mapper<LanguageColorApiData, LanguageColorEntity>() {

    override fun mapFrom(e: LanguageColorApiData): LanguageColorEntity {
        val entity = LanguageColorEntity()
        entity.color = e.color
        entity.url = e.url
        return entity
    }
}