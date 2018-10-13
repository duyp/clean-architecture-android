package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.addOnPageSelectedListener
import com.duyp.architecture.clean.android.powergit.setOnItemClickListener
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.drawer.DrawerHolder
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_navigation.*
import javax.inject.Inject

class MainActivity : ViewModelActivity<MainViewState, MainIntent, MainViewModel>() {

    @Inject lateinit var mDrawerHolder: DrawerHolder

    @Inject lateinit var mPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarShadow(true)
        pager.adapter = mPagerAdapter
        pager.addOnPageSelectedListener {
            onIntent(MainIntent.OnPageSelected(it))
        }
        bottomNavigation.setOnItemClickListener { _, position, fromUser ->
            if (position > 3) {
                showToastMessage("Coming soon...")
                bottomNavigation.post { bottomNavigation.setSelectedIndex(pager.currentItem, true) }
            }
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
            R.id.search -> SearchActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!mDrawerHolder.closeDrawer()) {
            super.onBackPressed()
        }
    }

    private fun setCurrentPage(position: Int) {
        if (position != pager.currentItem) {
            pager.currentItem = position
        }
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