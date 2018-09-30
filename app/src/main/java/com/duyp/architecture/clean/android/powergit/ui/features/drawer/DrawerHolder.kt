package com.duyp.architecture.clean.android.powergit.ui.features.drawer

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.duyp.androidutils.AlertUtils
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.domain.entities.UserEntity
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginActivity
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import com.duyp.architecture.clean.android.powergit.ui.widgets.AvatarLayout
import com.duyp.architecture.clean.android.powergit.withState
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DrawerHolder @Inject constructor(
        private val mActivity: AppCompatActivity,
        private val mAvatarLoader: AvatarLoader,
        private val mViewModelFactory: ViewModelProvider.Factory
) {

    private lateinit var mViewModel: DrawerViewModel
    private val mIntent : PublishSubject<DrawerIntent> = PublishSubject.create()

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavHeader: View

    fun init(drawerLayout: DrawerLayout) {

        mViewModel = ViewModelProviders.of(mActivity, mViewModelFactory).get(DrawerViewModel::class.java)
        mViewModel.processIntents(mIntent)

        mDrawer = drawerLayout
        val extrasNav = mDrawer.findViewById<NavigationView>(R.id.extrasNav)

        mNavHeader = extrasNav.getHeaderView(0)
        mNavHeader.setOnClickListener { onIntent(DrawerIntent.OpenUserProfile()) }

        extrasNav.setNavigationItemSelectedListener { item ->
            if (item.isChecked) {
                return@setNavigationItemSelectedListener false
            }
            when (item.itemId) {
                R.id.profile -> onIntent(DrawerIntent.OpenUserProfile())
                R.id.logout -> onIntent(DrawerIntent.LogoutIntent())
                else -> AlertUtils.showToastLongMessage(mActivity, "Comming soon!")
            }
            mDrawer.closeDrawer(GravityCompat.START)
            true
        }

        withState {
            updateUser(user)
            event(navigation) {
                when(this) {
                    DrawerNavigation.LOGIN -> {
                        LoginActivity.start(mActivity)
                    }
                    DrawerNavigation.MY_PROFILE -> {
                        onIntent(DrawerIntent.OpenUserProfile())
                    }
                }
            }
            event(refreshUser) {
                onIntent(DrawerIntent.RefreshUser())
            }
        }
        onIntent(DrawerIntent.RefreshUser())
    }

    /**
     * Close drawer
     *
     * @return true if drawer is closed, false if it already closed
     */
    fun closeDrawer(): Boolean {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    private fun updateUser(user: UserEntity?) {
        if (user != null) {
            mNavHeader.findViewById<TextView>(R.id.navFullName).text = user.displayName
            mNavHeader.findViewById<TextView>(R.id.navUsername).text = user.login
        } else {
            mNavHeader.findViewById<TextView>(R.id.navFullName).text = mActivity.getString(R.string.please_login)
            mNavHeader.findViewById<TextView>(R.id.navUsername).text = ""
        }
        mNavHeader.findViewById<AvatarLayout>(R.id.navAvatarLayout).bindData(mAvatarLoader, user)
    }

    private fun onIntent(intent: DrawerIntent) {
        mIntent.onNext(intent)
    }

    private fun withState(state: DrawerViewState.() -> Unit) {
        mActivity.withState(mViewModel, state)
    }
}