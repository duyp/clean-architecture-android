package com.duyp.architecture.clean.android.powergit.di.modules

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.duyp.androidutils.navigation.ChildFragmentNavigator
import com.duyp.androidutils.navigation.FragmentNavigator
import com.duyp.architecture.clean.android.powergit.di.qualifier.ChildFragmentManager
import dagger.Module
import dagger.Provides

@Module
abstract class FragmentModule<T: Fragment> {

    @Provides
    @ChildFragmentManager
    fun provideFragmentManager(fragment: T): FragmentManager {
        return fragment.childFragmentManager
    }

    @Provides
    fun provideNavigator(fragment: T): FragmentNavigator {
        return ChildFragmentNavigator(fragment)
    }
}
