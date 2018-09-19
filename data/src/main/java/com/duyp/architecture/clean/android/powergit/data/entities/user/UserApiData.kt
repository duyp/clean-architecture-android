package com.duyp.architecture.clean.android.powergit.data.entities.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserApiData(
        @SerializedName("id")
        var id: Long,

        @SerializedName("login")
        var login: String,

        @SerializedName("avatar_url")
        var avatarUrl: String,

        @SerializedName("gravatar_id")
        var gravatarId: String,

        @SerializedName("url")
        var url: String,

        @SerializedName("html_url")
        var htmlUrl: String,

        @SerializedName("followers_url")
        var followersUrl: String,

        @SerializedName("following_url")
        var followingUrl: String,

        @SerializedName("gists_url")
        var gistsUrl: String,

        @SerializedName("starred_url")
        var starredUrl: String,

        @SerializedName("subscriptions_url")
        var subscriptionsUrl: String,

        @SerializedName("organizations_url")
        var organizationsUrl: String,

        @SerializedName("repos_url")
        var reposUrl: String,

        @SerializedName("events_url")
        var eventsUrl: String,

        @SerializedName("received_events_url")
        var receivedEventsUrl: String,

        @SerializedName("type")
        var type: String? = null,

        @SerializedName("site_admin")
        var siteAdmin: Boolean? = null,

        @SerializedName("name")
        var name: String,

        @SerializedName("company")
        var company: String,

        @SerializedName("blog")
        var blog: String,

        @SerializedName("location")
        var location: String,

        @SerializedName("email")
        var email: String,

        @SerializedName("hireable")
        var hireable: Boolean? = null,

        @SerializedName("bio")
        var bio: String,

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

        // todo check this field
        var date: Date? = null,

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