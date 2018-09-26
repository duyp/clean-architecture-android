package com.duyp.architecture.clean.android.powergit

import com.duyp.architecture.clean.android.powergit.di.DaggerAppComponent

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class PowerGitApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
