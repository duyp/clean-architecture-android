package com.duyp.architecture.clean.android.powergit.domain.entities

data class TeamsEntity(

    val id: Long = 0,

    val url: String? = null,

    val name: String? = null,

    val slug: String? = null,

    val description: String? = null,

    val privacy: String? = null,

    val permission: String? = null,

    val membersUrl: String? = null,

    val repositoriesUrl: String? = null
)