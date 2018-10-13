package com.duyp.architecture.clean.android.powergit.domain.entities.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryBuilder
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState

enum class IssueType {
    CREATED,
    ASSIGNED,
    MENTIONED,
    PARTICIPATED
}

object IssueQueryProvider {

    fun getIssuesQuery(owner: String, repoName: String, @IssueState issueState: String): String {
        return QueryBuilder.issue {
            repo = "$owner/$repoName"
            state = issueState
        }
    }

    internal fun getMyIssuesQuery(username: String, @IssueState issueState: String): String {
        return QueryBuilder.issue {
            author = username
            state = issueState
        }
    }

    internal fun getAssignedIssuesQuery(username: String, @IssueState issueState: String): String {
        return QueryBuilder.issue {
            assignee = username
            state = issueState
        }
    }

    internal fun getMentionedIssuesQuery(username: String, @IssueState issueState: String): String {
        return QueryBuilder.issue {
            mentions = username
            state = issueState
        }
    }

    internal fun getParticipatedIssuesQuery(username: String, @IssueState issueState: String): String {
        return QueryBuilder.issue {
            involves = username
            state = issueState
        }
    }

    fun getQuery(username: String, type: IssueType, @IssueState state: String): String {
        return when(type) {
            IssueType.CREATED -> getMyIssuesQuery(username, state)
            IssueType.ASSIGNED -> getAssignedIssuesQuery(username, state)
            IssueType.MENTIONED -> getMentionedIssuesQuery(username, state)
            IssueType.PARTICIPATED -> getParticipatedIssuesQuery(username, state)
        }
    }
}