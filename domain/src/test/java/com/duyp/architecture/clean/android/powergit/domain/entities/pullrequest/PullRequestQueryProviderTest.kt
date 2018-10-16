package com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest

import com.duyp.architecture.clean.android.powergit.domain.assertEquals
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getAssignedPullRequestQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getMentionedPullRequestQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getMyPullRequestQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getParticipatedPullRequestQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getPullRequestQuery
import com.duyp.architecture.clean.android.powergit.domain.entities.pullrequest.PullRequestQueryProvider.getReviewRequestedPullRequestQuery
import org.junit.Test

class PullRequestQueryProviderTest {

    @Test
    fun getPullRequestQueryTest() {
        getPullRequestQuery("duyp", "clean-architecture", PullRequestState.CLOSED)
                .assertEquals("+type:pr+repo:duyp/clean-architecture+is:closed")
    }

    @Test
    fun getMyPullRequestQueryTest() {
        getMyPullRequestQuery("duyp", PullRequestState.OPEN)
                .assertEquals("+type:pr+author:duyp+is:open")
    }

    @Test
    fun getAssignedPullRequestQueryTest() {
        getAssignedPullRequestQuery("duyp", PullRequestState.ALL)
                .assertEquals("+type:pr+assignee:duyp+is:all")
    }

    @Test
    fun getMentionedPullRequestQueryTest() {
        getMentionedPullRequestQuery("duyp", PullRequestState.OPEN)
                .assertEquals("+type:pr+mentions:duyp+is:open")
    }

    @Test
    fun getReviewRequestsPullRequestQueryTest() {
        getReviewRequestedPullRequestQuery("duyp", PullRequestState.OPEN)
                .assertEquals("+type:pr+review-requested:duyp+is:open")
    }

    @Test
    fun getParticipatedPullRequestQueryTest() {
        getParticipatedPullRequestQuery("duyp", PullRequestState.ALL)
                .assertEquals("+type:pr+involves:duyp+is:all")
    }
}