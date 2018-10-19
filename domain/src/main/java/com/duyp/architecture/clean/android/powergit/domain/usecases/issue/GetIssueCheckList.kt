package com.duyp.architecture.clean.android.powergit.domain.usecases.issue

import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueCheckListInfo
import com.duyp.architecture.clean.android.powergit.domain.utils.totalMatches
import java.util.regex.Pattern
import javax.inject.Inject

class GetIssueCheckList @Inject constructor() {

    companion object {
        private const val PATTERN_UNCHECK = "\\[ ]"
        private const val PATTERN_CHECKED = "\\[x|X]"
    }

    fun get(issue: IssueEntity): IssueCheckListInfo {
        if (!issue.body.isNullOrEmpty()) {
            val unchecked = Pattern.compile(PATTERN_UNCHECK).matcher(issue.body).totalMatches()
            val checked = Pattern.compile(PATTERN_CHECKED).matcher(issue.body).totalMatches()
            return IssueCheckListInfo(
                    total = checked + unchecked,
                    done = checked
            )
        }
        return IssueCheckListInfo(0, 0)
    }
}