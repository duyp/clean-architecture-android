package com.duyp.architecture.clean.android.powergit.data.entities.authentication

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Authentication")
data class AuthenticationLocalData(

        @PrimaryKey
        var username: String,

        var auth: String
)