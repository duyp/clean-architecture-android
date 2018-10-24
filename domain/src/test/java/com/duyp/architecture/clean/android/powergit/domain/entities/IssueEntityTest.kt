package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.assert
import org.junit.Test

class IssueEntityTest {

    @Test
    fun correctChecklist() {
        var issue = IssueEntity(body = "Implement issue list:\\r\\n- [x] Basic list\\r\\n- [x] Filter state (open, closed)\\r\\n- [x] User issues: filter by type (create, assigned, participated, mentioned)\\r\\n- [x] Local cache (database)\\r\\n- [ ] Additional filters (label, milestones...)\\r\\n- [x] Search\\r\\n- [ ]  Unit tests\\r\\n\\r\\nIn screens:\\r\\n- [x] Main screen (my user issues)\\r\\n- [ ] Repo detail screen")
        issue.getChecklistInfo()?.assert {
            done == 6 && total == 9
        }

        issue = IssueEntity()
        issue.getChecklistInfo()?.assert {
            done == 0 && total == 0
        }

        issue = IssueEntity(body = "abcd 1234")
        issue.getChecklistInfo()?.assert {
            done == 0 && total == 0
        }

        issue = IssueEntity(body = "[x] [X]")
        issue.getChecklistInfo()?.assert {
            done == 2 && total == 2
        }

        issue = IssueEntity(body = "[] [] [ ] [ ] [x] [X] [ x ]")
        issue.getChecklistInfo()?.assert {
            done == 2 && total == 4
        }
    }
}