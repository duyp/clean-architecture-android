package com.duyp.architecture.clean.android.powergit.authentication;

import android.app.Activity;

public class PowerGitAuthService extends PowerGitAuthenticationService {

    @Override
    protected Class<? extends Activity> getAuthenticatorActivityClass() {
        // we don't support yet adding accounts over the account manager of the
        // device.
        return null;
    }

}
