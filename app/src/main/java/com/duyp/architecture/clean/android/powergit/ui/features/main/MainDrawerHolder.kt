package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.TextView
import com.duyp.androidutils.AlertUtils
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityContext
import com.duyp.architecture.clean.android.powergit.domain.entities.User
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import com.duyp.architecture.clean.android.powergit.ui.widgets.AvatarLayout
import javax.inject.Inject

class MainDrawerHolder @Inject constructor(
        @ActivityContext private val mContext: Context,
        private val mAvatarLoader: AvatarLoader
) {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavHeader: View

    var onProfileClick: (() -> Unit)? = null
    var onLogoutClick: (() -> Unit)? = null


    fun init(drawerLayout: DrawerLayout) {
        mDrawer = drawerLayout
        val extrasNav = mDrawer.findViewById<NavigationView>(R.id.extrasNav)

        mNavHeader = extrasNav.getHeaderView(0)
        mNavHeader.setOnClickListener { _ -> onProfileClick?.invoke() }

        extrasNav.setNavigationItemSelectedListener { item ->
            if (item.isChecked) {
                return@setNavigationItemSelectedListener false
            }
            when (item.itemId) {
                R.id.profile -> onProfileClick?.invoke()
                R.id.logout -> onLogoutClick?.invoke()
                else -> AlertUtils.showToastLongMessage(mContext, "Comming soon!")
            }
            mDrawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun updateUser(user: User?) {
        if (user != null) {
            mNavHeader.findViewById<TextView>(R.id.navFullName).text= user.displayName
            mNavHeader.findViewById<TextView>(R.id.navUsername).text= user.login
        } else {
            mNavHeader.findViewById<TextView>(R.id.navFullName).text= mContext.getString(R.string.please_login)
            mNavHeader.findViewById<TextView>(R.id.navUsername).text= ""
        }
        mNavHeader.findViewById<AvatarLayout>(R.id.navAvatarLayout).bindData(mAvatarLoader, user)
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
}