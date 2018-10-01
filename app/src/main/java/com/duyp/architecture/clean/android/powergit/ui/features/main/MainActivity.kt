package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.drawer.DrawerHolder
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : ViewModelActivity<MainViewState, MainIntent, MainViewModel>() {

    @Inject lateinit var mDrawerHolder: DrawerHolder

    @Inject lateinit var mPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pager.adapter = mPagerAdapter

        // init drawer
        mDrawerHolder.init(drawer)

        withState {

        }
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