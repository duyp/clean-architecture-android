package com.duyp.architecture.clean.android.powergit.data.di

import android.content.Context
import androidx.room.Room
import com.duyp.architecture.clean.android.powergit.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class DatabaseModule() {

    @Provides @Singleton fun provideAppDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "powergit-database").build();

    @Provides @Singleton fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

}