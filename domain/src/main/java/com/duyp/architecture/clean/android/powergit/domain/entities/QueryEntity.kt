package com.duyp.architecture.clean.android.powergit.domain.entities

class QueryEntity private constructor(
        var type: String,
        var repo: String? = null,
        var repoOwner: String? = null,
        var author: String? = null,
        var assignee: String? = null,
        var mentions: String? = null,
        var reviewRequested: String? = null,
        var involves: String? = null,
        var state: String? = null,
        var searchTerm: String? = null,
        init: QueryEntity.() -> Unit
) {

    init {
        this.init()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("+type:$type")
        if (repo != null && repoOwner != null) {
            sb.append("+repo:$repoOwner/$repo")
        }
        author?.let { sb.append("+author:$it") }
        assignee?.let { sb.append("+assignee:$it") }
        mentions?.let { sb.append("+mentions:$it") }
        reviewRequested?.let { sb.append("+review-requested:$it") }
        involves?.let { sb.append("+involves:$it") }
        state?.let { sb.append("+is:$it") }
        searchTerm?.let { sb.append("+$it") }
        return sb.toString()
    }

    companion object {
        const val TYPE_ISSUE = "issue"
        const val TYPE_PULL_REQUEST = "pr"

        fun getPullRequestQuery(init: QueryEntity.() -> Unit) = QueryEntity(type = TYPE_PULL_REQUEST, init = init)
        fun getIssueQuery(init: QueryEntity.() -> Unit) = QueryEntity(type = TYPE_ISSUE, init = init)
    }
}