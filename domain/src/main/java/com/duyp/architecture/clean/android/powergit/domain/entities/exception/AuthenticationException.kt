package com.duyp.architecture.clean.android.powergit.domain.entities.exception

/**
 * Represent authentication error, such as api response 401 or error when attempting to get current user if there are
 * no logged in user
 */
class AuthenticationException(message: String = ""): Exception(message)