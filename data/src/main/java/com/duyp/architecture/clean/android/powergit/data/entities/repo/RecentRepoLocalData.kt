package com.duyp.architecture.clean.android.powergit.data.entities.repo

import android.arch.persistence.room.Entity
import java.util.*

@Entity(tableName = "RecentRepository", primaryKeys = ["repoId"])
data class RecentRepoLocalData(

        var repoId: Long,

        var date: Date
)