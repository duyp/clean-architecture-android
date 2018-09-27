package com.duyp.architecture.clean.android.powergit.data.entities.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserApiData(
        @SerializedName("id")
        var id: Long = 0,

        @SerializedName("login")
        var login: String? = null,

        @SerializedName("avatar_url")
        var avatarUrl: String? = null,

        @SerializedName("gravatar_id")
        var gravatarId: String? = null,

        @SerializedName("url")
        var url: String? = null,

        @SerializedName("html_url")
        var htmlUrl: String? = null,

        @SerializedName("followers_url")
        var followersUrl: String? = null,

        @SerializedName("following_url")
        var followingUrl: String? = null,

        @SerializedName("gists_url")
        var gistsUrl: String? = null,

        @SerializedName("starred_url")
        var starredUrl: String? = null,

        @SerializedName("subscriptions_url")
        var subscriptionsUrl: String? = null,

        @SerializedName("organizations_url")
        var organizationsUrl: String? = null,

        @SerializedName("repos_url")
        var reposUrl: String? = null,

        @SerializedName("events_url")
        var eventsUrl: String? = null,

        @SerializedName("received_events_url")
        var receivedEventsUrl: String? = null,

        @SerializedName("type")
        var type: String? = null,

        @SerializedName("site_admin")
        var siteAdmin: Boolean? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("company")
        var company: String? = null,

        @SerializedName("blog")
        var blog: String? = null,

        @SerializedName("location")
        var location: String? = null,

        @SerializedName("email")
        var email: String? = null,

        @SerializedName("hireable")
        var hireable: Boolean? = null,

        @SerializedName("bio")
        var bio: String? = null,

        @SerializedName("public_repos")
        var publicRepos: Long = 0,

        @SerializedName("public_gists")
        var publicGists: Long = 0,

        @SerializedName("followers")
        var followers: Long = 0,

        @SerializedName("following")
        var following: Long = 0,

        @SerializedName("created_at")
        var createdAt: Date? = null,

        @SerializedName("updated_at")
        var updatedAt: Date? = null,

        @SerializedName("private_gists")
        var privateGists: Long = 0,

        @SerializedName("total_private_repos")
        var totalPrivateRepos: Long = 0,

        @SerializedName("owned_private_repos")
        var ownedPrivateRepos: Long = 0,

        @SerializedName("disk_usage")
        var diskUsage: Long = 0,

        @SerializedName("collaborators")
        var collaborators: Long = 0,

        @SerializedName("two_factor_authentication")
        var twoFactorAuthentication: Boolean? = null,

        //@SerializedName("plan")
        //var plan: Plan,

        @SerializedName("contributions")
        var contributions: Int = 0
)