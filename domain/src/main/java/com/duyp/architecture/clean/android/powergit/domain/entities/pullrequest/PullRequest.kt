package com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryBuilder

enum class PullRequestType {
    CREATED,
    ASSIGNED,
    MENTIONED,
    REVIEW,
    PARTICIPATED
}

enum class PullRequestState {
    OPEN, CLOSED, ALL
}

object PullRequestQueryProvider {

    fun getPullRequestQuery(owner: String, repoName: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            repo = "$owner/$repoName"
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getMyPullRequestQuery(username: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            author = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getAssignedPullRequestQuery(username: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            assignee = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getMentionedPullRequestQuery(username: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            mentions = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getReviewRequestedPullRequestQuery(username: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            reviewRequested = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getParticipatedPullRequestQuery(username: String, pullRequestState: PullRequestState): String {
        return QueryBuilder.pullRequest {
            involves = username
            state = pullRequestState.name.toLowerCase()
        }
    }
}