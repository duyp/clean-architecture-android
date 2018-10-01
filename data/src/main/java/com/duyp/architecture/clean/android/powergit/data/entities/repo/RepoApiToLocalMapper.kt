package com.duyp.architecture.clean.android.powergit.data.entities.repo

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class RepoApiToLocalMapper: Mapper<RepoApiData, RepoLocalData>() {

    private val mUserApiToLocalMapper = UserApiToLocalMapper()

    override fun mapFrom(e: RepoApiData): RepoLocalData {
        val localData = RepoLocalData(e.id)
        e.owner?.let { localData.owner = mUserApiToLocalMapper.mapFrom(it) }
        localData.name = e.name
        localData.fullName = e.fullName
        localData.htmlUrl = e.htmlUrl
        localData.description = e.description
        localData.url = e.url
        localData.homepage = e.homepage
        localData.defaultBranch = e.defaultBranch
        localData.language = e.language
        localData.createdAt = e.createdAt
        localData.updatedAt = e.updatedAt
        localData.pushedAt = e.pushedAt
        localData.size = e.size
        localData.stargazersCount = e.stargazersCount
        localData.watchersCount = e.watchersCount
        localData.forksCount = e.forksCount
        localData.openIssuesCount = e.openIssuesCount
        localData.forks = e.forks
        localData.openIssues = e.openIssues
        localData.watchers = e.watchers
        localData.private = e.private
        localData.fork = e.fork
        localData.hasIssues = e.hasIssues
        localData.hasProjects = e.hasProjects
        localData.hasDownloads = e.hasDownloads
        localData.hasWiki = e.hasWiki
        localData.hasPages = e.hasPages
        return localData
    }
}