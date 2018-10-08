package com.duyp.architecture.clean.android.powergit.data.utils

import android.accounts.Account
import android.accounts.AccountManager
import com.duyp.architecture.clean.android.powergit.data.di.AccountType

import javax.inject.Inject

/**
 * Helper class for [AccountManager]
 */
class AccountManagerHelper @Inject internal constructor(
        private val mAccountManager: AccountManager,
        @AccountType private val mAccountType: String) {

    /**
     * Get all accounts in [AccountManager] with PowerGit account type
     */
    fun getAllAccounts(): Array<out Account> = mAccountManager.getAccountsByType(mAccountType)

    /**
     * Get password for existing account in android account manager with given account name
     *
     * @param name account name
     *
     * @return null if no account existed yet or no password set
     */
    fun getPassword(name: String?): String? {
        val account = getExistingAccount(name)
        return if (account != null) mAccountManager.getPassword(account) else null
    }

    /**
     * Get authentication token saved for given account's name
     * @param name name of account
     * @return null if no account found
     */
    fun getAuth(name: String?): String? {
        val account = getExistingAccount(name)
        return if (account != null) mAccountManager.peekAuthToken(account, "") else null
    }

    /**
     * Save an account to android account manager. If the account already existed (same account's name), it will be
     * updated with new password
     *
     * @param name account name
     * @param password account password
     * @param authToken account authToken
     */
    fun saveAccount(name: String, password: String, authToken: String) {
        var account = getExistingAccount(name)
        if (account == null) {
            account = Account(name, mAccountType)
            mAccountManager.addAccountExplicitly(account, password, null)
        } else
            mAccountManager.setPassword(account, password)

        mAccountManager.setAuthToken(account, "", authToken)
    }

    /**
     * Logout account by clearing password and authentication
     */
    fun logoutAccount(name: String) {
        val account = getExistingAccount(name)
        if (account != null) {
            // remove the local auth token
            val auth = mAccountManager.peekAuthToken(account, "")
            mAccountManager.invalidateAuthToken(mAccountType, auth)
            // remove password
            mAccountManager.setPassword(account, null)
        }
    }

    /**
     * Get existing account in android account manager with given account name
     * @param name account name
     * @return null if no account existed yet
     */
    private fun getExistingAccount(name: String?): Account? {
        if (name == null || name.isEmpty()) return null

        val accounts = mAccountManager.getAccountsByType(mAccountType)
        for (account in accounts) {
            if (account.name.equals(name))
                return account
        }
        return null
    }
}
