package com.duyp.architecture.clean.android.powergit.domain.entities.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState

enum class MyIssueTypeEntity {
    CREATED,
    ASSIGNED,
    MENTIONED,
    PARTICIPATED
}

object IssueQueryProvider {

    fun getIssuesQuery(owner: String, repoName: String, @IssueState issueState: String): QueryEntity {
        return QueryEntity.getIssueQuery {
            repo = repoName
            repoOwner = owner
            state = issueState
        }
    }

    internal fun getMyIssuesQuery(username: String, @IssueState issueState: String): QueryEntity {
        return QueryEntity.getIssueQuery {
            author = username
            state = issueState
        }
    }

    internal fun getAssignedIssuesQuery(username: String, @IssueState issueState: String): QueryEntity {
        return QueryEntity.getIssueQuery {
            assignee = username
            state = issueState
        }
    }

    internal fun getMentionedIssuesQuery(username: String, @IssueState issueState: String): QueryEntity {
        return QueryEntity.getIssueQuery {
            mentions = username
            state = issueState
        }
    }

    internal fun getParticipatedIssuesQuery(username: String, @IssueState issueState: String): QueryEntity {
        return QueryEntity.getIssueQuery {
            involves = username
            state = issueState
        }
    }

    fun getMyIssueQuery(username: String, type: MyIssueTypeEntity, @IssueState state: String): QueryEntity {
        return when(type) {
            MyIssueTypeEntity.CREATED -> getMyIssuesQuery(username, state)
            MyIssueTypeEntity.ASSIGNED -> getAssignedIssuesQuery(username, state)
            MyIssueTypeEntity.MENTIONED -> getMentionedIssuesQuery(username, state)
            MyIssueTypeEntity.PARTICIPATED -> getParticipatedIssuesQuery(username, state)
        }
    }
}