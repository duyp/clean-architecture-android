package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity


data class PayloadEntity(

        var action: String? = null,

        var refType: String? = null,

        var description: String? = null,

        var before: String? = null,

        var head: String? = null,

        var ref: String? = null,

        var size: Int = 0,

        var forkee: RepoEntity? = null,

        var issue: IssueEntity? = null,

        var pullRequest: PullRequestEntity? = null,

        var comment: CommentEntity? = null,

        var commitComment: CommentEntity? = null,

        var team: TeamsEntity? = null,

        var download: ReleaseAssetEntity? = null,

        var gist: GistEntity? = null,

        var release: ReleaseEntity? = null,

        var target: UserEntity? = null,

        var member: UserEntity? = null,

        var user: UserEntity? = null,

        var blockedUser: UserEntity? = null,

        var organization: UserEntity? = null,

        var invitation: UserEntity? = null,

        var pages: List<WikiEntity>? = null,

        var commits: List<CommitEntity>? = null
)