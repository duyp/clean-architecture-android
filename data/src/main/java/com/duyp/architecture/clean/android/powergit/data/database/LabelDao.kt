package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.duyp.architecture.clean.android.powergit.data.entities.label.IssueLabelsLocalData
import com.duyp.architecture.clean.android.powergit.data.entities.label.LabelLocalData

@Dao
abstract class LabelDao: BaseDao<LabelLocalData>() {

    @Query("SELECT * FROM Label INNER JOIN IssueLabels ON Label.id = IssueLabels.id WHERE IssueLabels.issueId = :issueId")
    abstract fun getLabels(issueId: Long): List<LabelLocalData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertForIssue(data: List<IssueLabelsLocalData>)
}
