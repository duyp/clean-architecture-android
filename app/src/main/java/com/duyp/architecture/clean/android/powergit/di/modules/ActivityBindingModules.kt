package com.duyp.architecture.clean.android.powergit.di.modules

import com.duyp.architecture.clean.android.powergit.di.AppComponent
import com.duyp.architecture.clean.android.powergit.di.scopes.ActivityScoped
import com.duyp.architecture.clean.android.powergit.ui.features.drawer.DrawerModule
import com.duyp.architecture.clean.android.powergit.ui.features.event.EventModule
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginActivity
import com.duyp.architecture.clean.android.powergit.ui.features.login.LoginModule
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainActivity
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainActivityModule
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainModule
import com.duyp.architecture.clean.android.powergit.ui.features.repo.list.RepoListModule
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchRepoActivity
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchRepoActivityModule
import com.duyp.architecture.clean.android.powergit.ui.features.search.SearchRepoModule
import com.duyp.architecture.clean.android.powergit.ui.features.splash.SplashActivity
import com.duyp.architecture.clean.android.powergit.ui.features.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModules {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity() : LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SplashModule::class])
    internal abstract fun splashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [
        MainModule::class,
        MainActivityModule::class,
        DrawerModule::class,
        RepoListModule::class,
        EventModule::class
    ])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [
        SearchRepoModule::class,
        SearchRepoActivityModule::class
    ])
    internal abstract fun searchRepoActivity(): SearchRepoActivity

}
