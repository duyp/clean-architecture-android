package com.duyp.architecture.clean.android.powergit.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

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