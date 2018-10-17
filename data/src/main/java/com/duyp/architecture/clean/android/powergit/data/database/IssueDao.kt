package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.issue.IssueLocalData
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class IssueDao: BaseDao<IssueLocalData>() {

    @Query("SELECT id FROM Issue WHERE title LIKE :searchTerm ORDER BY createdAt DESC")
    abstract fun searchByTitle(searchTerm: String): Single<List<Long>>

    @Query("SELECT id FROM Issue WHERE repoOwner = :repoOwner AND repoName = :repoName AND state = :state ORDER BY createdAt DESC")
    abstract fun getByRepo(repoOwner: String, repoName: String, state: String?): Single<List<Long>>

    @Query("SELECT id FROM Issue WHERE user_login = :author AND state = :state ORDER BY createdAt DESC")
    abstract fun getByAuthor(author: String, state: String?): Single<List<Long>>

    @Query("SELECT id FROM Issue WHERE assignee_login = :assignee AND state = :state ORDER BY createdAt DESC")
    abstract fun getByAssignee(assignee: String, state: String?): Single<List<Long>>

    @Query("SELECT * FROM Issue WHERE id = :id")
    abstract fun getById(id: Long): Maybe<IssueLocalData>
}