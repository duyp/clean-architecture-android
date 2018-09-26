package com.duyp.architecture.clean.android.powergit.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import timber.log.Timber;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

public class PowerGitUserAuthenticator extends AbstractAccountAuthenticator {

	private final Context _context;
	private final Class<? extends Activity> _authenticatorActivityClass;

    public PowerGitUserAuthenticator(final Context context,
                                     final Class<? extends Activity> authenticatorActivity) {
		super(context);
        _context = context;
        _authenticatorActivityClass = authenticatorActivity;
    }

    @Override
    public Bundle editProperties(final AccountAuthenticatorResponse response,
                                 final String accountType) {
        return null;
	}

	@Override
    public Bundle addAccount(final AccountAuthenticatorResponse response,
                             final String accountType, final String authTokenType,
                             final String[] requiredFeatures, final Bundle options) {

		final Bundle bundle = new Bundle();
        if(_authenticatorActivityClass != null) {
            Timber.d("addAccount");

			final Intent intent = new Intent(_context,
                    _authenticatorActivityClass);
			intent.putExtra(PowerGitAuthenticationService.ARG_ACCOUNT_TYPE,
					accountType);
			intent.putExtra(PowerGitAuthenticationService.ARG_AUTH_TYPE,
					authTokenType);
			intent.putExtra(
					PowerGitAuthenticationService.ARG_IS_ADDING_NEW_ACCOUNT, true);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
					response);

			bundle.putParcelable(AccountManager.KEY_INTENT, intent);
			return bundle;
		} else
			bundle.putInt(AccountManager.KEY_ERROR_CODE,
					AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);

		return bundle;
	}

	@Override
    public Bundle confirmCredentials(final AccountAuthenticatorResponse response,
                                     final Account account, final Bundle options) {
        return null;
    }

    @Override
    public Bundle getAuthToken(final AccountAuthenticatorResponse response,
                               final Account account, final String authTokenType, final Bundle options) {

        Timber.d("getAuthToken");

		final AccountManager am = AccountManager.get(_context);

        final String authToken = am.peekAuthToken(account, authTokenType);
        Timber.d("peekAuthToken returned - " + authToken);

		// If we get an authToken - we return it
		if (!TextUtils.isEmpty(authToken)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			return result;
		}

		final Bundle bundle = new Bundle();
        if(_authenticatorActivityClass != null) {
			// If we get here, then we couldn't access the user's password - so
			// we
			// need to re-prompt them for their credentials. We do that by
			// creating
			// an intent to display our AuthenticatorActivity.
			final Intent intent = new Intent(_context,
                    _authenticatorActivityClass);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
					response);
			intent.putExtra(PowerGitAuthenticationService.ARG_ACCOUNT_TYPE,
					account.type);
			intent.putExtra(PowerGitAuthenticationService.ARG_AUTH_TYPE,
					authTokenType);
			intent.putExtra(PowerGitAuthenticationService.ARG_ACCOUNT_NAME,
					account.name);

			bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		} else
			bundle.putInt(AccountManager.KEY_ERROR_CODE,
					AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);

		return bundle;
	}

	@Override
    public String getAuthTokenLabel(final String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(final AccountAuthenticatorResponse response,
                                    final Account account, final String authTokenType, final Bundle options) {
		return null;
	}

	@Override
    public Bundle hasFeatures(final AccountAuthenticatorResponse response,
                              final Account account, final String[] features) {
		final Bundle result = new Bundle();
		result.putBoolean(KEY_BOOLEAN_RESULT, false);
		return result;
	}
}
