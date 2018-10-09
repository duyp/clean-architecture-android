package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duyp.androidutils.navigation.Navigator
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.ui.base.BaseActivity
import javax.inject.Inject

class SearchRepoActivity: BaseActivity() {

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.replaceFragment(R.id.container, SearchRepoFragment())
        }
        setToolbarTitle("Search Repositories")
    }

    override fun getLayoutResource() = R.layout.container_with_toolbar

    override fun canBack() = true

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchRepoActivity::class.java))
        }
    }
}