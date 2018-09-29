package com.duyp.architecture.clean.android.powergit.ui.features.splash

import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginActivity
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainActivity
import com.duyp.architecture.clean.android.powergit.withState

class SplashActivity: ViewModelActivity<SplashState, Any, SplashViewModel>() {

    override fun getLayoutResource(): Int {
        return NO_LAYOUT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        withState(mViewModel) {
            event(navigation) {
                when (this) {
                    Navigation.MAIN -> MainActivity.start(this@SplashActivity)
                    Navigation.LOGIN -> LoginActivity.start(this@SplashActivity)
                }
                finish()
            }
        }
    }
}