package com.duyp.architecture.clean.android.powergit.data.entities.repo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "Repository", indices = [Index("ownerUsername")])
data class RepoLocalData(

        @PrimaryKey
        var id: Long,

        var ownerUsername: String? = null,

        var name: String? = null,

        var fullName: String? = null,

        var htmlUrl: String? = null,

        var description: String? = null,

        var url: String? = null,

        var homepage: String? = null,

        var defaultBranch: String? = null,

        var language: String? = null,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var pushedAt: Date? = null,

        var size: Long = 0,

        var stargazersCount: Long = 0,

        var watchersCount: Long = 0,

        var forksCount: Long = 0,

        var openIssuesCount: Long = 0,

        var forks: Long = 0,

        var openIssues: Long = 0,

        var watchers: Long = 0,

        var private: Boolean = false,

        var fork: Boolean? = null,

        var hasIssues: Boolean = false,

        var hasProjects: Boolean = false,

        var hasDownloads: Boolean = false,

        var hasWiki: Boolean = false,

        var hasPages: Boolean = false
)