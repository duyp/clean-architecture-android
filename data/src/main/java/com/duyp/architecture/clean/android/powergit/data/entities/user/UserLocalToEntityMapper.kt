package com.duyp.architecture.clean.android.powergit.data.entities.user

import androidx.annotation.NonNull
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.User

class UserLocalToEntityMapper() : Mapper<UserLocalData, User> (){

    @NonNull override fun mapFrom(@NonNull e: UserLocalData): User {
        val userEntity = User()
        userEntity.id = e.id
        userEntity.login = e.login
        userEntity.avatarUrl = e.avatarUrl
        userEntity.gravatarId = e.gravatarId
        userEntity.url = e.url
        userEntity.htmlUrl = e.htmlUrl
        userEntity.followersUrl = e.followersUrl
        userEntity.followingUrl = e.followingUrl
        userEntity.gistsUrl = e.gistsUrl
        userEntity.starredUrl = e.starredUrl
        userEntity.subscriptionsUrl = e.subscriptionsUrl
        userEntity.organizationsUrl = e.organizationsUrl
        userEntity.reposUrl = e.reposUrl
        userEntity.eventsUrl = e.eventsUrl
        userEntity.receivedEventsUrl = e.receivedEventsUrl
        userEntity.type = e.type
        userEntity.siteAdmin = e.siteAdmin
        userEntity.name = e.name
        userEntity.company = e.company
        userEntity.blog = e.blog
        userEntity.location = e.location
        userEntity.email = e.email
        userEntity.hireable = e.hireable
        userEntity.bio = e.bio
        userEntity.publicRepos = e.publicRepos
        userEntity.publicGists = e.publicGists
        userEntity.followers = e.followers
        userEntity.following = e.following
        userEntity.createdAt = e.createdAt
        userEntity.updatedAt = e.updatedAt
        userEntity.privateGists = e.privateGists
        userEntity.totalPrivateRepos = e.totalPrivateRepos
        userEntity.ownedPrivateRepos = e.ownedPrivateRepos
        userEntity.diskUsage = e.diskUsage
        userEntity.collaborators = e.collaborators
        userEntity.twoFactorAuthentication = e.twoFactorAuthentication
        userEntity.contributions = e.contributions
        return userEntity
    }

}