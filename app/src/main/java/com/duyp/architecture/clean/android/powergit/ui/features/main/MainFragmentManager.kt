package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.duyp.architecture.clean.android.powergit.R
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
        val tag = getFragmentTag(position)
        var fragment: Fragment? = mFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = getFragmentAtPosition(position)
            hideAllFragmentsAnd { add(containerId, fragment, tag) }
        } else {
            if (fragment.isHidden) {
                hideAllFragmentsAnd { show(fragment) }
            }
        }
    }

    // hide all fragments and also apply more transactions
    private fun hideAllFragmentsAnd(alsoFunc: (FragmentTransaction.() -> FragmentTransaction)) {
        mFragmentManager.inTransaction {
            setCustomAnimations(R.anim.fade_in_short, R.anim.fade_out_short)
            for (fragment in mFragmentManager.fragments) {
                hide(fragment)
            }
            return@inTransaction alsoFunc()
        }
    }

    private fun getFragmentTag(position: Int) = position.toString()

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