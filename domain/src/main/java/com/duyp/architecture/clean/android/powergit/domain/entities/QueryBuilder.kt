package com.duyp.architecture.clean.android.powergit.domain.entities

class QueryBuilder private constructor(
        var type: String,
        var repo: String? = null,
        var author: String? = null,
        var assignee: String? = null,
        var mentions: String? = null,
        var reviewRequested: String? = null,
        var involves: String? = null,
        var state: String? = null,
        init: QueryBuilder.() -> Unit
) {

    init {
        this.init()
    }

    fun build(): String {
        val sb = StringBuilder()
        sb.append("+type:$type")
        repo?.let { sb.append("+repo:$it") }
        author?.let { sb.append("+author:$it") }
        assignee?.let { sb.append("+assignee:$it") }
        mentions?.let { sb.append("+mentions:$it") }
        reviewRequested?.let { sb.append("+review-requested:$it") }
        involves?.let { sb.append("+involves:$it") }
        state.let { sb.append("+is:$it") }
        return sb.toString()
    }

    companion object {
        fun pullRequest(init: QueryBuilder.() -> Unit) = QueryBuilder(type = "pr", init = init).build()
        fun issue(init: QueryBuilder.() -> Unit) = QueryBuilder(type = "issue", init = init).build()
    }
}