package com.duyp.architecture.clean.android.powergit.domain.entities.issue

import com.duyp.architecture.clean.android.powergit.domain.assertEquals
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import org.junit.Test

class IssueQueryProviderTest {

    @Test
    fun getIssueQueryTest() {
        IssueQueryProvider.getIssuesQuery("duyp", "clean-architecture", IssueState.CLOSED)
                .toString()
                .assertEquals("+type:issue+repo:duyp/clean-architecture+is:closed")
    }

    @Test
    fun getMyIssueQueryTest() {
        IssueQueryProvider.getMyIssuesQuery("duyp", IssueState.OPEN)
                .toString()
                .assertEquals("+type:issue+author:duyp+is:open")
    }

    @Test
    fun getAssignedIssueQueryTest() {
        IssueQueryProvider.getAssignedIssuesQuery("duyp", IssueState.ALL)
                .toString()
                .assertEquals("+type:issue+assignee:duyp+is:all")
    }

    @Test
    fun getMentionedIssueQueryTest() {
        IssueQueryProvider.getMentionedIssuesQuery("duyp", IssueState.OPEN)
                .toString()
                .assertEquals("+type:issue+mentions:duyp+is:open")
    }

    @Test
    fun getParticipatedIssueQueryTest() {
        IssueQueryProvider.getParticipatedIssuesQuery("duyp", IssueState.ALL)
                .toString()
                .assertEquals("+type:issue+involves:duyp+is:all")
    }
}