package com.duyp.architecture.clean.android.powergit.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * service class binding the IS24 account authenticator.
 * 
 * @author marsta
 * 
 */
public abstract class PowerGitAuthenticationService extends Service {

	public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
	public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
	public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT";
	public final static String PARAM_USER_PASS = "USER_PASS";

	protected AbstractAccountAuthenticator getAuthenticator() {
		return new PowerGitUserAuthenticator(this.getApplicationContext(), getAuthenticatorActivityClass());
	}

	protected abstract Class<? extends Activity> getAuthenticatorActivityClass();

    @Override
    public IBinder onBind(Intent intent) {
        AbstractAccountAuthenticator authenticator = getAuthenticator();
        return authenticator.getIBinder();
    }
}
