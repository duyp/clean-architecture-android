package com.duyp.architecture.clean.android.powergit.data.entities.label

import android.arch.persistence.room.Entity

@Entity(tableName = "Label", primaryKeys = ["id"])
data class LabelLocalData(

        var id: Long = 0,

        var url: String? = null,

        var name: String? = null,

        var color: String? = null,

        var _default: Boolean = false
)