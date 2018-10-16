package com.duyp.architecture.clean.android.powergit.data.entities.label

import android.arch.persistence.room.Entity

@Entity(tableName = "IssueLabels", primaryKeys = ["id", "issueId"])
data class IssueLabelsLocalData(

        var id: Long = 0,

        var issueId: Long = 0

)