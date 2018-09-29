package com.duyp.architecture.clean.android.powergit.di.modules

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.duyp.androidutils.glide.loader.SimpleGlideLoader
import com.duyp.androidutils.navigation.ChildFragmentNavigator
import com.duyp.androidutils.navigation.FragmentNavigator
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityContext
import com.duyp.architecture.clean.android.powergit.di.qualifier.ChildFragmentManager
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import dagger.Module
import dagger.Provides

@Module
abstract class FragmentModule<T: Fragment> {

    @Provides @ActivityContext fun provideContext(fragment: T) = fragment.context

    @Provides fun provideActivity(fragment: T) = fragment.activity

    @Provides
    fun provideDefaultGlide(fragment: T) = SimpleGlideLoader(fragment)

    @Provides
    fun provideAvatarLoader(fragment: T) = AvatarLoader(fragment)

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
