package com.duyp.architecture.clean.android.powergit.di.modules

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.duyp.androidutils.glide.loader.SimpleGlideLoader
import com.duyp.androidutils.navigation.ActivityNavigator
import com.duyp.androidutils.navigation.Navigator
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityContext
import com.duyp.architecture.clean.android.powergit.di.qualifier.ActivityFragmentManager
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule<T: AppCompatActivity> {

    @Provides @ActivityContext fun provideContext(activity: T) = activity as Context

    @Provides fun provideActivity(activity: T) = activity as AppCompatActivity

    @Provides
    fun provideDefaultGlide(activity: T) = SimpleGlideLoader(activity)

    @Provides
    fun provideAvatarLoader(activity: T) = AvatarLoader(activity)

    @Provides
    @ActivityFragmentManager
    fun provideFragmentManager(activity: T): FragmentManager {
        return activity.getSupportFragmentManager()
    }

    @Provides
    fun provideNavigator(activity: T): Navigator {
        return ActivityNavigator(activity)
    }
}
