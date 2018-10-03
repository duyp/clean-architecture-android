package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityFragmentManager
import com.duyp.architecture.clean.android.powergit.putEnum
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventListFragment
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventType
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoListFragment
import com.duyp.architecture.clean.android.powergit.withArguments
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(@ActivityFragmentManager fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EventListFragment().withArguments { putEnum(BundleConstants.EXTRA_TYPE, EventType.RECEIVED) }
            1 -> RepoListFragment()
            else -> RepoListFragment().withArguments { putString(BundleConstants.EXTRA_DATA, "hungpn") }
        }
    }

    override fun getCount(): Int {
        return 3
    }

}