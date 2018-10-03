package com.duyp.architecture.clean.android.powergit.data.entities.event

import com.duyp.architecture.clean.android.powergit.data.entities.comment.CommentApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.commit.CommitApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.gist.GistApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.pullrequest.PullRequestApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.release.ReleaseApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.release.ReleaseAssetApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.teams.TeamApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.wiki.WikiApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper
import com.duyp.architecture.clean.android.powergit.domain.entities.PayloadEntity

class PayloadApiToEntityMapper: Mapper<PayloadApiData, PayloadEntity>() {

    private val mUserApiToEntityMapper = UserApiToEntityMapper()

    private val mCommitApiToEntityMapper = CommitApiToEntityMapper()

    private val mCommentApiToEntityMapper = CommentApiToEntityMapper()

    private val mPullRequestApiToEntityMapper = PullRequestApiToEntityMapper()

    private val mIssueApiToEntityMapper = IssueApiToEntityMapper()

    private val mTeamApiToEntityMapper = TeamApiToEntityMapper()

    private val mReleaseAssetApiToEntityMapper = ReleaseAssetApiToEntityMapper()

    private val mReleaseApiToEntityMapper = ReleaseApiToEntityMapper()

    private val mGistApiToEntityMapper = GistApiToEntityMapper()

    private val mWikiApiToEntityMapper = WikiApiToEntityMapper()

    override fun mapFrom(e: PayloadApiData): PayloadEntity {
        val entity = PayloadEntity()
        entity.action = e.action
        entity.refType = e.refType
        entity.description = e.description
        entity.before = e.before
        entity.head = e.head
        entity.ref = e.ref
        entity.size = e.size
        entity.forkee = e.forkee
        entity.issue = e.issue?.let { mIssueApiToEntityMapper.mapFrom(it) }
        entity.pullRequest = e.pullRequest?.let { mPullRequestApiToEntityMapper.mapFrom(it) }
        entity.comment = e.comment?.let { mCommentApiToEntityMapper.mapFrom(it) }
        entity.commitComment = e.commitComment?.let { mCommentApiToEntityMapper.mapFrom(it) }
        entity.team = e.team?.let { mTeamApiToEntityMapper.mapFrom(it) }
        entity.download = e.download?.let { mReleaseAssetApiToEntityMapper.mapFrom(it) }
        entity.gist = e.gist?.let { mGistApiToEntityMapper.mapFrom(it) }
        entity.release = e.release?.let { mReleaseApiToEntityMapper.mapFrom(it) }
        entity.target = e.target?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.member = e.member?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.user = e.user?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.blockedUser = e.blockedUser?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.organization = e.organization?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.invitation = e.invitation?.let { mUserApiToEntityMapper.mapFrom(it) }
        entity.pages = e.pages?.let { mWikiApiToEntityMapper.mapFrom(it) }
        entity.commits = e.commits?.let { mCommitApiToEntityMapper.mapFrom(it) }
        return entity
    }
}