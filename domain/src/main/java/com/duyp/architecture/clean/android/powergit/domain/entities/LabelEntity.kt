package com.duyp.architecture.clean.android.powergit.domain.entities


data class LabelEntity(

        var id: Long? = null,

        var url: String? = null,

        var name: String? = null,

        var color: String? = null,

        var _default: Boolean = false
)