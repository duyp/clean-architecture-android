package com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest

import com.duyp.architecture.clean.android.powergit.domain.entities.QueryEntity

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

    fun getPullRequestQuery(owner: String, repoName: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            repo = repoName
            repoOwner = owner
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getMyPullRequestQuery(username: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            author = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getAssignedPullRequestQuery(username: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            assignee = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getMentionedPullRequestQuery(username: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            mentions = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getReviewRequestedPullRequestQuery(username: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            reviewRequested = username
            state = pullRequestState.name.toLowerCase()
        }
    }

    fun getParticipatedPullRequestQuery(username: String, pullRequestState: PullRequestState): QueryEntity {
        return QueryEntity.getPullRequestQuery {
            involves = username
            state = pullRequestState.name.toLowerCase()
        }
    }
}