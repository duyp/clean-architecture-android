package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityFragmentManager
import com.duyp.architecture.clean.android.powergit.inTransaction
import com.duyp.architecture.clean.android.powergit.putEnum
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventListFragment
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventType
import com.duyp.architecture.clean.android.powergit.ui.features.issue.list.IssueListFragment
import com.duyp.architecture.clean.android.powergit.ui.features.issue.list.IssueListType
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoListFragment
import com.duyp.architecture.clean.android.powergit.withArguments

internal class MainFragmentManager constructor(
        @ActivityFragmentManager private val mFragmentManager: FragmentManager,
        private val containerId: Int
) {

    internal fun setPosition(position: Int) {
        var fragment: Fragment? = mFragmentManager.findFragmentByTag(position.toString())
        if (fragment == null) {
            fragment = getFragmentAtPosition(position)
            hideAllFragments(alsoAdd = fragment, addToPosition = position)
        } else {
            if (fragment.isHidden) {
                hideAllFragments()
                mFragmentManager.inTransaction { show(fragment) }
            }
        }
    }

    // hide all mFragments
    private fun hideAllFragments(alsoAdd: Fragment? = null, addToPosition: Int = 0) {
        mFragmentManager.inTransaction {
            for (fragment in mFragmentManager.fragments) {
                hide(fragment)
            }
            alsoAdd?.let {
                add(containerId, it, addToPosition.toString())
                show(it)
            }
            return@inTransaction this
        }
    }

    private fun getFragmentAtPosition(position: Int): Fragment? {
        return when (position) {
            0 -> EventListFragment().withArguments { putEnum(BundleConstants.EXTRA_TYPE, EventType.RECEIVED) }
            1 -> RepoListFragment()
            2 -> IssueListFragment().withArguments { putEnum(BundleConstants.EXTRA_TYPE, IssueListType.USER_ISSUES) }
            else -> IssueListFragment().withArguments {
                putEnum(BundleConstants.EXTRA_TYPE, IssueListType.REPO_ISSUES)
                putString(BundleConstants.EXTRA_USERNAME, "duyp")
                putString(BundleConstants.EXTRA_REPO_NAME, "clean-architecture-android")
            }
        }
    }
}