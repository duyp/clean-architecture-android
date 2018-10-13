package com.duyp.architecture.clean.android.powergit.domain.entities.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryBuilder

enum class IssueType {
    CREATED,
    ASSIGNED,
    MENTIONED,
    PARTICIPATED
}

enum class IssueState {
    OPEN, CLOSED, ALL
}

object IssueQueryProvider {

    fun getIssuesQuery(owner: String, repoName: String, issueState: IssueState): String {
        return QueryBuilder.issue {
            repo = "$owner/$repoName"
            state = issueState.name.toLowerCase()
        }
    }

    internal fun getMyIssuesQuery(username: String, issueState: IssueState): String {
        return QueryBuilder.issue {
            author = username
            state = issueState.name.toLowerCase()
        }
    }

    internal fun getAssignedIssuesQuery(username: String, issueState: IssueState): String {
        return QueryBuilder.issue {
            assignee = username
            state = issueState.name.toLowerCase()
        }
    }

    internal fun getMentionedIssuesQuery(username: String, issueState: IssueState): String {
        return QueryBuilder.issue {
            mentions = username
            state = issueState.name.toLowerCase()
        }
    }

    internal fun getParticipatedIssuesQuery(username: String, issueState: IssueState): String {
        return QueryBuilder.issue {
            involves = username
            state = issueState.name.toLowerCase()
        }
    }

    fun getQuery(username: String, type: IssueType, state: IssueState): String {
        return when(type) {
            IssueType.CREATED -> getMyIssuesQuery(username, state)
            IssueType.ASSIGNED -> getAssignedIssuesQuery(username, state)
            IssueType.MENTIONED -> getMentionedIssuesQuery(username, state)
            IssueType.PARTICIPATED -> getParticipatedIssuesQuery(username, state)
        }
    }
}