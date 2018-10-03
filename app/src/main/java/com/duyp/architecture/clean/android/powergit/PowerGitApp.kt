package com.duyp.architecture.clean.android.powergit

import com.duyp.architecture.clean.android.powergit.di.DaggerAppComponent

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class PowerGitApp : DaggerApplication() {

    companion object {
        private lateinit var instance: PowerGitApp

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
