package com.duyp.architecture.clean.android.powergit.data.entities.milestone

import com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiData
import java.util.*

class MilestoneApiData(

        var id: Long = 0,

        var url: String? = null,

        var title: String? = null,

        var state: String? = null,

        var description: String? = null,

        var htmlUr: String? = null,

        var number: Int = 0,

        var openIssues: Int = 0,

        var closedIssues: Int = 0,

        var createdAt: Date? = null,

        var updatedAt: Date? = null,

        var closedAt: Date? = null,

        var dueOn: Date? = null,

        var creator: UserApiData? = null
)