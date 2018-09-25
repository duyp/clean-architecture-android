package com.duyp.architecture.clean.android.powergit.data.entities.user

import androidx.annotation.NonNull
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class UserApiToLocalMapper() : Mapper<UserApiData, UserLocalData> (){

    @NonNull override fun mapFrom(@NonNull e: UserApiData): UserLocalData {
        val localData = UserLocalData(e.id)
        localData.login = e.login
        localData.avatarUrl = e.avatarUrl
        localData.gravatarId = e.gravatarId
        localData.url = e.url
        localData.htmlUrl = e.htmlUrl
        localData.followersUrl = e.followersUrl
        localData.followingUrl = e.followingUrl
        localData.gistsUrl = e.gistsUrl
        localData.starredUrl = e.starredUrl
        localData.subscriptionsUrl = e.subscriptionsUrl
        localData.organizationsUrl = e.organizationsUrl
        localData.reposUrl = e.reposUrl
        localData.eventsUrl = e.eventsUrl
        localData.receivedEventsUrl = e.receivedEventsUrl
        localData.type = e.type
        localData.siteAdmin = e.siteAdmin
        localData.name = e.name
        localData.company = e.company
        localData.blog = e.blog
        localData.location = e.location
        localData.email = e.email
        localData.hireable = e.hireable
        localData.bio = e.bio
        localData.publicRepos = e.publicRepos
        localData.publicGists = e.publicGists
        localData.followers = e.followers
        localData.following = e.following
        localData.createdAt = e.createdAt
        localData.updatedAt = e.updatedAt
        localData.privateGists = e.privateGists
        localData.totalPrivateRepos = e.totalPrivateRepos
        localData.ownedPrivateRepos = e.ownedPrivateRepos
        localData.diskUsage = e.diskUsage
        localData.collaborators = e.collaborators
        localData.twoFactorAuthentication = e.twoFactorAuthentication
        localData.contributions = e.contributions
        return localData
    }

}