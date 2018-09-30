package com.duyp.architecture.clean.android.powergit.data.entities.repo

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.google.gson.annotations.SerializedName
import java.util.*

data class RepoApiData (

        @SerializedName("id")
        var id: Long,

        @SerializedName("owner")
        var owner: UserApiData? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("full_name")
        var fullName: String? = null,

        @SerializedName("html_url")
        var htmlUrl: String? = null,

        @SerializedName("description")
        var description: String? = null,

        @SerializedName("url")
        var url: String? = null,

        @SerializedName("homepage")
        var homepage: String? = null,

        @SerializedName("default_branch")
        var defaultBranch: String? = null,

        @SerializedName("language")
        var language: String? = null,

        @SerializedName("created_at")
        var createdAt: Date? = null,

        @SerializedName("updated_at")
        var updatedAt: Date? = null,

        @SerializedName("pushed_at")
        var pushedAt: Date? = null,

        @SerializedName("size")
        var size: Long = 0,

        @SerializedName("stargazers_count")
        var stargazersCount: Long = 0,

        @SerializedName("watchers_count")
        var watchersCount: Long = 0,

        @SerializedName("forks_count")
        var forksCount: Long = 0,

        @SerializedName("open_issues_count")
        var openIssuesCount: Long = 0,

        @SerializedName("forks")
        var forks: Long = 0,

        @SerializedName("open_issues")
        var openIssues: Long = 0,

        @SerializedName("watchers")
        var watchers: Long = 0,

        @SerializedName("private")
        var private: Boolean = false,

        @SerializedName("fork")
        var fork: Boolean? = null,

        @SerializedName("has_issues")
        var hasIssues: Boolean = false,

        @SerializedName("has_projects")
        var hasProjects: Boolean = false,

        @SerializedName("has_downloads")
        var hasDownloads: Boolean = false,

        @SerializedName("has_wiki")
        var hasWiki: Boolean = false,

        @SerializedName("has_pages")
        var hasPages: Boolean = false
)
