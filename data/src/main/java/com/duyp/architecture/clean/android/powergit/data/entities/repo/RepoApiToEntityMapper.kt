package com.duyp.architecture.clean.android.powergit.data.entities.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity

class RepoApiToEntityMapper : Mapper<RepoApiData, RepoEntity>(){

    override fun mapFrom(e: RepoApiData): RepoEntity {
        val entity = RepoEntity(e.id)
        entity.ownerLogin = e.owner?.login
        entity.ownerAvatarUrl = e.owner?.avatarUrl
        entity.name = e.name
        entity.fullName = e.fullName
        entity.htmlUrl = e.htmlUrl
        entity.description = e.description
        entity.url = e.url
        entity.homepage = e.homepage
        entity.defaultBranch = e.defaultBranch
        entity.language = e.language
        entity.createdAt = e.createdAt
        entity.updatedAt = e.updatedAt
        entity.pushedAt = e.pushedAt
        entity.size = e.size
        entity.stargazersCount = e.stargazersCount
        entity.watchersCount = e.watchersCount
        entity.forksCount = e.forksCount
        entity.openIssuesCount = e.openIssuesCount
        entity.forks = e.forks
        entity.openIssues = e.openIssues
        entity.watchers = e.watchers
        entity.private = e.private
        entity.fork = e.fork
        entity.hasIssues = e.hasIssues
        entity.hasProjects = e.hasProjects
        entity.hasDownloads = e.hasDownloads
        entity.hasWiki = e.hasWiki
        entity.hasPages = e.hasPages
        return entity

    }
}