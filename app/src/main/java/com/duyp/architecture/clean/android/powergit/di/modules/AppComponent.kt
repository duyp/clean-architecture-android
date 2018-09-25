package com.duyp.architecture.clean.android.powergit.di.modules

import com.duyp.architecture.clean.android.powergit.di.PowerGitApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] is the module from Dagger.Android that helps with the
 * generation and location of subcomponents.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModules::class,
        PowerGitViewModelFactoryModule::class,
        AppModule::class,
        NetworkModule::class,
        DataModule::class
    ]
)
interface AppComponent : AndroidInjector<PowerGitApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PowerGitApp>()
}
