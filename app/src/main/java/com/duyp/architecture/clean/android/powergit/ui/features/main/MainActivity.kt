package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : ViewModelActivity<MainViewState, MainIntent, MainViewModel>() {

    @Inject lateinit var mDrawerHolder: MainDrawerHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init drawer
        mDrawerHolder.init(drawer)
        mDrawerHolder.onLogoutClick = { onIntent(MainIntent.LogoutIntent()) }
        mDrawerHolder.onProfileClick = { onIntent(MainIntent.OpenUserProfile()) }

        withState {
            mDrawerHolder.updateUser(user)
            event(navigation) {
                when(this) {
                    MainNavigation.LOGIN -> {
                        LoginActivity.start(this@MainActivity)
                    }
                    MainNavigation.MY_PROFILE -> {
                        onIntent(MainIntent.OpenUserProfile())
                    }
                }
            }
            event(refreshUser) {
                onIntent(MainIntent.RefreshUser())
            }
        }
        onIntent(MainIntent.RefreshUser())
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        if (!mDrawerHolder.closeDrawer()) {
            super.onBackPressed()
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
        }
    }
}