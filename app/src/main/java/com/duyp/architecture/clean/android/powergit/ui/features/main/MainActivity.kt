package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.drawer.DrawerHolder
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_navigation.*
import javax.inject.Inject

class MainActivity : ViewModelActivity<MainViewState, MainIntent, MainViewModel>() {

    @Inject lateinit var mDrawerHolder: DrawerHolder

    @Inject lateinit var mPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = mPagerAdapter

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                onIntent(MainIntent.OnPageSelected(position))
            }

        })
        bottomNavigation.setOnMenuItemClickListener(object: BottomNavigation.OnMenuItemSelectionListener {

            override fun onMenuItemSelect(id: Int, position: Int, fromUser: Boolean) {
                if (position > 3) {
                    showToastMessage("Coming soon...")
                    bottomNavigation.post { bottomNavigation.setSelectedIndex(pager.currentItem, true) }
                }
                if (fromUser) {
                    onIntent(MainIntent.OnPageSelected(position))
                }
            }

            override fun onMenuItemReselect(id: Int, position: Int, fromUser: Boolean) {}

        })
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