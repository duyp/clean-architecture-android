package com.duyp.architecture.clean.android.powergit.ui.features.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.addSimpleTextChangedListener
import com.duyp.architecture.clean.android.powergit.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchRepoActivity: BaseActivity() {

    override fun getLayoutResource() = R.layout.activity_search

    override fun canBack() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        edtSearch.addSimpleTextChangedListener {
            getSearchFragment()?.onSearch(it)
        }
    }

    private fun getSearchFragment(): SearchRepoFragment? {
        val f = supportFragmentManager.findFragmentById(R.id.fragmentSearch)
        if (f != null && f is SearchRepoFragment && f.isAdded) {
            return f
        }
        return null
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchRepoActivity::class.java))
        }
    }
}