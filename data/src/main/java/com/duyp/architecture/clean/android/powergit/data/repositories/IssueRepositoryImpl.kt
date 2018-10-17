package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.IssueService
import com.duyp.architecture.clean.android.powergit.data.database.IssueDao
import com.duyp.architecture.clean.android.powergit.data.database.LabelDao
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueListApiToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.data.entities.label.IssueLabelsLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelApiToLocalMapper
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelLocalToEntityMapper
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.QueryEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.IssueRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class IssueRepositoryImpl @Inject constructor(
        private val mIssueService: IssueService,
        private val mIssueDao: IssueDao,
        private val mLabelDao: LabelDao
): IssueRepository {

    private val mIssueListApiToEntityMapper = IssueListApiToEntityMapper()

    private val mIssueApiToLocalMapper = IssueApiToLocalMapper()

    private val mIssueLocalToEntityMapper = IssueLocalToEntityMapper()

    private val mLabelApiToLocalMapper = LabelApiToLocalMapper()

    private val mLabelLocalToEntityMapper = LabelLocalToEntityMapper()

    override fun getIssueList(query: QueryEntity, page: Int): Single<ListEntity<Long>> {
        return mIssueService.getIssues(query.toString(), page)
                .doOnSuccess {
                    mIssueDao.insertList(mIssueApiToLocalMapper.mapFrom(it.items))
                    it.items.forEach { issueApiData ->
                        if (issueApiData.labels?.isNotEmpty() == true) {
                            // save all labels
                            mLabelDao.insertList(mLabelApiToLocalMapper.mapFrom(issueApiData.labels))
                            // save labels for each issue
                            issueApiData.labels?.map { label ->
                                IssueLabelsLocalData(label.id, issueApiData.id)
                            }?.let { labelList -> mLabelDao.insertForIssue(labelList) }
                        }
                    }
                }
                .map { mIssueListApiToEntityMapper.mapFrom(it) }
                .onErrorResumeNext { throwable ->
                    if (page == ListEntity.STARTING_PAGE && query.type == QueryEntity.TYPE_ISSUE) {
                        var local: Single<List<Long>>? = null
                        if (query.repo != null && query.repoOwner != null) {
                            local = mIssueDao.getByRepo(query.repoOwner!!, query.repo!!, query.state)

                        } else if (query.author != null) {
                            local = mIssueDao.getByAuthor(query.author!!, query.state)
                        } else if (query.assignee != null) {
                            local = mIssueDao.getByAssignee(query.assignee!!, query.state)
                        }
                        // no offline supports for involves
                        if (local != null) {
                            return@onErrorResumeNext local.map {
                                ListEntity(items = it, isOfflineData = true, apiError = throwable)
                            }
                        }
                    }
                    return@onErrorResumeNext Single.error(throwable)
                }
    }

    override fun searchLocalIssue(searchTerm: String): Single<List<Long>> {
        return mIssueDao.searchByTitle("%$searchTerm%")
    }

    override fun getById(id: Long): Maybe<IssueEntity> {
        return mIssueDao.getById(id)
                .map { mIssueLocalToEntityMapper.mapFrom(it) }
                .map {
                    it.labels = mLabelLocalToEntityMapper.mapFrom(mLabelDao.getLabels(it.id))
                    it
                }
    }
}