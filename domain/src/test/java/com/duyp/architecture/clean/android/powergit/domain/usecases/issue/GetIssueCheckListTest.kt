package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.assert
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import org.junit.Test

class GetIssueCheckListTest {

    private val mGetIssueCheckList = GetIssueCheckList()
    @Test
    fun get() {
        val result = mGetIssueCheckList.get(IssueEntity(body = "Implement issue list:\\r\\n- [x] Basic list\\r\\n- [x] Filter state (open, closed)\\r\\n- [x] User issues: filter by type (create, assigned, participated, mentioned)\\r\\n- [x] Local cache (database)\\r\\n- [ ] Additional filters (label, milestones...)\\r\\n- [x] Search\\r\\n- [ ]  Unit tests\\r\\n\\r\\nIn screens:\\r\\n- [x] Main screen (my user issues)\\r\\n- [ ] Repo detail screen"))
        result.assert {
            done == 6 && total == 9
        }

        val result1 = mGetIssueCheckList.get(IssueEntity())
        result1.assert {
            done == 0 && total == 0
        }

        val result2 = mGetIssueCheckList.get(IssueEntity(body = "abcd 1234"))
        result2.assert {
            done == 0 && total == 0
        }

        val result3 = mGetIssueCheckList.get(IssueEntity(body = "[x] [X]"))
        result3.assert {
            done == 2 && total == 2
        }

        val result4 = mGetIssueCheckList.get(IssueEntity(body = "[] [] [ ] [ ] [x] [X] [ x ]"))
        result4.assert {
            done == 2 && total == 4
        }
    }
}