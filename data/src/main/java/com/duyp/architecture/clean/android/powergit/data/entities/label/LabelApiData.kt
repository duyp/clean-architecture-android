package com.duyp.architecture.clean.android.powergit.data.entities.label

import com.google.gson.annotations.SerializedName

data class LabelApiData(

        @SerializedName("id")
        var id: Long? = null,

        @SerializedName("url")
        var url: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("color")
        var color: String? = null,

        @SerializedName("default")
        var _default: Boolean = false
)