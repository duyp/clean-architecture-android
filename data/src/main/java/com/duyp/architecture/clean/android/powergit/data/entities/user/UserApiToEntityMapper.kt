package com.duyp.architecture.clean.android.powergit.data.entities.user

import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity

class UserApiToEntityMapper : Mapper<UserApiData, UserEntity> (){

    override fun mapFrom(e: UserApiData): UserEntity {
        val entity = UserEntity(e.id)
        entity.login = e.login
        entity.avatarUrl = e.avatarUrl
        entity.gravatarId = e.gravatarId
        entity.url = e.url
        entity.htmlUrl = e.htmlUrl
        entity.followersUrl = e.followersUrl
        entity.followingUrl = e.followingUrl
        entity.gistsUrl = e.gistsUrl
        entity.starredUrl = e.starredUrl
        entity.subscriptionsUrl = e.subscriptionsUrl
        entity.organizationsUrl = e.organizationsUrl
        entity.reposUrl = e.reposUrl
        entity.eventsUrl = e.eventsUrl
        entity.receivedEventsUrl = e.receivedEventsUrl
        entity.type = e.type
        entity.siteAdmin = e.siteAdmin
        entity.name = e.name
        entity.company = e.company
        entity.blog = e.blog
        entity.location = e.location
        entity.email = e.email
        entity.hireable = e.hireable
        entity.bio = e.bio
        entity.publicRepos = e.publicRepos
        entity.publicGists = e.publicGists
        entity.followers = e.followers
        entity.following = e.following
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.privateGists = e.privateGists
        entity.totalPrivateRepos = e.totalPrivateRepos
        entity.ownedPrivateRepos = e.ownedPrivateRepos
        entity.diskUsage = e.diskUsage
        entity.collaborators = e.collaborators
        entity.twoFactorAuthentication = e.twoFactorAuthentication
        entity.contributions = e.contributions
        return entity
    }

}