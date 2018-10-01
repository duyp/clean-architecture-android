package com.duyp.architecture.clean.android.powergit.ui.features.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityFragmentManager
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoListFragment
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(@ActivityFragmentManager fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RepoListFragment()
            else -> RepoListFragment()
        }
    }

    override fun getCount(): Int {
        return 1
    }


}