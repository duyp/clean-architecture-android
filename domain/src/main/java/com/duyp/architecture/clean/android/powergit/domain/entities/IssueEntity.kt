package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.issue.IssueCheckListInfo
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import com.duyp.architecture.clean.android.powergit.domain.utils.totalMatches
import java.util.*
import java.util.regex.Pattern

data class IssueEntity(

        var id: Long = 0,

        var url: String? = null,

        private var body: String? = null,

        var title: String? = null,

        var comments: Int = 0,

        var number: Int = 0,

        var locked: Boolean = false,

        @IssueState
        var state: String? = null, //todo IssueState2

        var repoUrl: String? = null,

        var bodyHtml: String? = null,

        var htmlUrl: String? = null,

        var closedAt: Date? = null,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var repoName: String? = null,

        var repoOwner: String? = null,

        var user: UserEntity? = null,

        var assignee: UserEntity? = null,

        var closedBy: UserEntity? = null,

        var assignees: List<UserEntity>? = null,

        var labels: List<LabelEntity>? = null,

        var milestone: MilestoneEntity? = null,

        var repository: RepoEntity? = null,

        var pullRequest: PullRequestEntity? = null,

        var reactions: ReactionsEntity? = null,

        private var checkListInfo: IssueCheckListInfo? = null
) {

    companion object {
        private const val PATTERN_UNCHECK = "\\[ ]"
        private const val PATTERN_CHECKED = "\\[x|X]"
    }

    /**
     * Set issue body as well as extracting [IssueCheckListInfo] from it
     */
    fun setBody(body: String?) {
        this.body = body
        this.checkListInfo = if (!this.body.isNullOrEmpty()) {
            val unchecked = Pattern.compile(PATTERN_UNCHECK).matcher(this.body).totalMatches()
            val checked = Pattern.compile(PATTERN_CHECKED).matcher(this.body).totalMatches()
            IssueCheckListInfo(
                    total = checked + unchecked,
                    done = checked
            )
        } else {
            IssueCheckListInfo(0, 0)
        }
    }

    fun getBody() = body

    fun getChecklistInfo() = checkListInfo
}