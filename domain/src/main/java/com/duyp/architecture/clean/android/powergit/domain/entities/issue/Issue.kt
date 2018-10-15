package com.duyp.architecture.clean.android.powergit.domain.entities.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryBuilder
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState

enum class MyIssueType {
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

    fun getMyIssueQuery(username: String, type: MyIssueType, @IssueState state: String): String {
        return when(type) {
            MyIssueType.CREATED -> getMyIssuesQuery(username, state)
            MyIssueType.ASSIGNED -> getAssignedIssuesQuery(username, state)
            MyIssueType.MENTIONED -> getMentionedIssuesQuery(username, state)
            MyIssueType.PARTICIPATED -> getParticipatedIssuesQuery(username, state)
        }
    }
}