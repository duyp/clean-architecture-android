package com.duyp.architecture.clean.android.powergit.data.entities.event

import com.duyp.architecture.clean.android.powergit.data.entities.comment.CommentApiData
import com.duyp.architecture.clean.android.powergit.data.entities.commit.CommitApiData
import com.duyp.architecture.clean.android.powergit.data.entities.gist.GistApiData
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueApiData
import com.duyp.architecture.clean.android.powergit.data.entities.pullrequest.PullRequestApiData
import com.duyp.architecture.clean.android.powergit.data.entities.release.ReleaseApiData
import com.duyp.architecture.clean.android.powergit.data.entities.release.ReleaseAssetApiData
import com.duyp.architecture.clean.android.powergit.data.entities.teams.TeamApiData
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import com.duyp.architecture.clean.android.powergit.data.entities.wiki.WikiApiData
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity


data class PayloadApiData(

        var action: String? = null,

        var refType: String? = null,

        var description: String? = null,

        var before: String? = null,

        var head: String? = null,

        var ref: String? = null,

        var size: Int = 0,

        var forkee: RepoEntity? = null,

        var issue: IssueApiData? = null,

        var pullRequest: PullRequestApiData? = null,

        var comment: CommentApiData? = null,

        var commitComment: CommentApiData? = null,

        var team: TeamApiData? = null,

        var download: ReleaseAssetApiData? = null,

        var gist: GistApiData? = null,

        var release: ReleaseApiData? = null,

        var target: UserApiData? = null,

        var member: UserApiData? = null,

        var user: UserApiData? = null,

        var blockedUser: UserApiData? = null,

        var organization: UserApiData? = null,

        var invitation: UserApiData? = null,

        var pages: List<WikiApiData>? = null,

        var commits: List<CommitApiData>? = null
)