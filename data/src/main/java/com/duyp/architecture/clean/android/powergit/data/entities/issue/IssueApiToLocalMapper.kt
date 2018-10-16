package com.duyp.architecture.clean.android.powergit.data.entities.issue

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.utils.PullsIssuesParser
import com.duyp.architecture.clean.android.powergit.domain.entities.Mapper

class IssueApiToLocalMapper: Mapper<IssueApiData, IssueLocalData>() {

    private val mUserApiToLocalMapper = UserApiToLocalMapper()

    override fun mapFrom(e: IssueApiData): IssueLocalData {
        val localData = IssueLocalData()
        localData.id = e.id
        localData.url = e.url
        localData.body = e.body
        localData.title = e.title
        localData.comments = e.comments
        localData.number = e.number
        localData.locked = e.locked
        localData.state = e.state
        localData.repoUrl = e.repoUrl
        localData.bodyHtml = e.bodyHtml
        localData.htmlUrl = e.htmlUrl
        localData.closedAt = e.closedAt
        localData.createdAt = e.createdAt
        localData.updatedAt = e.updatedAt
        localData.user = e.user?.let { mUserApiToLocalMapper.mapFrom(it) }
        e.htmlUrl?.let { url ->
            PullsIssuesParser.getForIssue(url)?.also {
                localData.repoName = it.repoId
                localData.repoOwner = it.login
            }
        }
        localData.assignee = e.assignee?.let { mUserApiToLocalMapper.mapFrom(it) }
//        localData.closedBy = e.closedBy
//        localData.assignees = e.assignees
//        localData.labels = e.labels
//        localData.milestone = e.milestone
//        localData.repository = e.repository
//        localData.pullRequest = e.pullRequest
//        localData.reactions = e.reactions
        return localData
    }
}