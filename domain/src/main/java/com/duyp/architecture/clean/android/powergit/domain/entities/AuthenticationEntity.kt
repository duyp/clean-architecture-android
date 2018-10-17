package com.duyp.architecture.clean.android.powergit.domain.entities

data class AuthenticationEntity(
        var username: String,
        var password: String,
        var token: String
)