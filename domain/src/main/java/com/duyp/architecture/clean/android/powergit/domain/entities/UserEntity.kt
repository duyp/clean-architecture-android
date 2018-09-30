package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil
import java.util.*

data class UserEntity (

    var id: Long? = null,

    var login: String? = null,

    var avatarUrl: String? = null,

    var gravatarId: String? = null,

    var url: String? = null,

    var htmlUrl: String? = null,

    var followersUrl: String? = null,

    var followingUrl: String? = null,

    var gistsUrl: String? = null,

    var starredUrl: String? = null,

    var subscriptionsUrl: String? = null,

    var organizationsUrl: String? = null,

    var reposUrl: String? = null,

    var eventsUrl: String? = null,

    var receivedEventsUrl: String? = null,

    var type: String? = null,

    var siteAdmin: Boolean? = null,

    var name: String? = null,

    var company: String? = null,

    var blog: String? = null,

    var location: String? = null,

    var email: String? = null,

    var hireable: Boolean? = null,

    var bio: String? = null,

    var publicRepos: Long = 0,

    var publicGists: Long = 0,

    var followers: Long = 0,

    var following: Long = 0,

    var createdAt: Date? = null,

    var updatedAt: Date? = null,

    var privateGists: Long = 0,

    var totalPrivateRepos: Long = 0,

    var ownedPrivateRepos: Long = 0,

    var diskUsage: Long = 0,

    var collaborators: Long = 0,

    var twoFactorAuthentication: Boolean? = null,

    //var plan: Plan? = null,

    var contributions: Int = 0
) {

    val displayName: String?
        get() = if (CommonUtil.isEmpty(name)) login else name

    val isOrganizationType: Boolean
        get() = type != null && type!!.equals("Organization", ignoreCase = true)

    fun equals(userLogin: String): Boolean {
        return this.login == userLogin
    }

    fun equals(user: UserEntity?): Boolean {
        return user != null && id == user.id
    }
}
