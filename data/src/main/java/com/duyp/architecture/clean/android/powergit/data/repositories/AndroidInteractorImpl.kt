package com.duyp.architecture.clean.android.powergit.data.repositories

import android.util.Base64
import com.duyp.architecture.clean.android.powergit.domain.repositories.AndroidInteractor
import javax.inject.Inject

class AndroidInteractorImpl @Inject constructor(): AndroidInteractor {

    override fun getBasicAuth(username: String, password: String): String {
        val credentials = "$username:$password"
        return "Basic " + Base64.encodeToString(credentials.toByteArray(), 2)
    }

}