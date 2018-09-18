package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.repositories.UserRepository

class LoginUser(private val mUserRepository: UserRepository) {

    fun login(username: String, password: String) = mUserRepository.login(username, password)
}