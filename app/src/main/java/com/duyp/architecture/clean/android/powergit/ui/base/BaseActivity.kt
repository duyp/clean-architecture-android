package com.duyp.architecture.clean.android.powergit.ui.base

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainActivity
import com.duyp.architecture.clean.android.powergit.ui.helper.ViewHelper
import com.evernote.android.state.StateSaver
import dagger.android.support.DaggerAppCompatActivity
import java.util.*

abstract class BaseActivity : DaggerAppCompatActivity() {

    protected var appBar: AppBarLayout? = null

    protected var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val i = getLayoutResource()
        if (i != ViewModelActivity.NO_LAYOUT) setContentView(i)

        setupLayoutStableFullscreen()

        restoreState(savedInstanceState)

        appBar = findViewById(R.id.appbar)
        toolbar = findViewById(R.id.toolbar)
        setupToolbarAndStatusBar()

        if (shouldPostponeTransition()) {
            ActivityCompat.postponeEnterTransition(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    abstract fun getLayoutResource(): Int

    /**
     * @return true if should use back button on toolbar
     */
    protected abstract fun canBack(): Boolean

    /**
     * @return true if this activity should use layout stable fullscreen (status bar overlap activity's content)
     */
    protected fun shouldUseLayoutStableFullscreen(): Boolean {
        return false
    }

    /**
     * @return true if this activity should postpone transition (in case of destination view is in viewpager)
     */
    protected fun shouldPostponeTransition(): Boolean {
        return false
    }

    /**
     * @return true if should use transparent status bar
     */
    protected fun isTransparentStatusBar(): Boolean {
        return true
    }

    protected fun changeStatusBarColor(isTransparent: Boolean) {
        if (!isTransparent) {
            window.statusBarColor = ViewHelper.getPrimaryDarkColor(this)
        }
    }

    protected fun hideShowShadow(show: Boolean) {
        if (appBar != null) {
            appBar!!.elevation = if (show) resources.getDimension(R.dimen.xx_tiny) else 0.0f
        }
    }

    private fun setupLayoutStableFullscreen() {
        if (shouldUseLayoutStableFullscreen()) {
            val decorView = window.decorView
            // Hide the status bar.
            val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = uiOptions
        }
    }

    private fun setupToolbarAndStatusBar() {
        changeStatusBarColor(isTransparentStatusBar())
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (canBack()) {
                if (supportActionBar != null) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    if (canBack()) {
                        val navIcon = getToolbarNavigationIcon(toolbar!!)
                        navIcon?.setOnLongClickListener {
                            MainActivity.start(this@BaseActivity)
                            finish()
                            true
                        }
                    }
                }
            }
        }
    }

    private fun getToolbarNavigationIcon(toolbar: android.support.v7.widget.Toolbar): View? {
        val hadContentDescription = TextUtils.isEmpty(toolbar.navigationContentDescription)
        val contentDescription = if (!hadContentDescription) toolbar.navigationContentDescription.toString() else "navigationIcon"
        toolbar.navigationContentDescription = contentDescription
        val potentialViews = ArrayList<View>()
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        var navIcon: View? = null
        if (potentialViews.size > 0) {
            navIcon = potentialViews[0]
        }
        if (hadContentDescription) toolbar.navigationContentDescription = null
        return navIcon
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            StateSaver.restoreInstanceState(this, savedInstanceState)
        }
    }

    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     *
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     * lives inside a Fragment hosted by the called Timeline).
     *
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     * asynchronously load/scale a bitmap before the transition can begin).
     *
     * (3) Inside a LoaderCallback's onLoadFinished() method (if the shared
     * element depends on data queried by a Loader).
     */
    fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition()
                        }
                        return true
                    }
                })
    }
}
