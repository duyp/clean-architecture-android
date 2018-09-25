package com.duyp.architecture.clean.android.powergit.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.duyp.architecture.clean.android.powergit.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class DataModule() {

    @Provides @Singleton fun provideAppDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "powergit-database").build();

    @Provides @Singleton fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

}