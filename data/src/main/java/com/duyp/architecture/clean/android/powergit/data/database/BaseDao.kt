package com.duyp.architecture.clean.android.powergit.data.database

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(list: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(t: T)

    @Delete
    abstract fun delete(t: T)
}