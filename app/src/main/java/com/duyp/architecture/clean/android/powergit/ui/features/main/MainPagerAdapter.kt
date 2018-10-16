package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityFragmentManager
import com.duyp.architecture.clean.android.powergit.putEnum
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventListFragment
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventType
import com.duyp.architecture.clean.android.powergit.ui.features.issue.list.IssueListFragment
import com.duyp.architecture.clean.android.powergit.ui.features.issue.list.IssueListType
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoListFragment
import com.duyp.architecture.clean.android.powergit.withArguments
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(@ActivityFragmentManager fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
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

    override fun getCount(): Int {
        return 4
    }

}