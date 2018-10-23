package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.setOnItemClickListener
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.drawer.DrawerHolder
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchActivity
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchTab
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_navigation.*
import javax.inject.Inject

class MainActivity : ViewModelActivity<MainViewState, MainIntent, MainViewModel>() {

    @Inject lateinit var mDrawerHolder: DrawerHolder

    private lateinit var mMainFragmentManager: MainFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarShadow(true)
        setToolbarIcon(R.drawable.ic_menu)
        mMainFragmentManager = MainFragmentManager(supportFragmentManager, R.id.container)

        bottomNavigation.setOnItemClickListener { _, position, fromUser ->
            if (fromUser) {
                onIntent(MainIntent.OnPageSelected(position))
            }
        }

        mDrawerHolder.init(drawer)

        withState {
            setCurrentPage(currentPage)
        }
    }

    override fun canBack() = false

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> mDrawerHolder.openDrawer()
            R.id.search -> {
                val tab = when (mViewModel.state.value?.currentPage) {
                    2 -> SearchTab.ISSUE
                    else -> SearchTab.REPO
                }
                SearchActivity.start(this, tab)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!mDrawerHolder.closeDrawer()) {
            super.onBackPressed()
        }
    }

    private fun setCurrentPage(position: Int) {
        setToolbarShadow(position < 2)
        mMainFragmentManager.setPosition(position)
        bottomNavigation.setSelectedIndex(position, true)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
        }
    }
}