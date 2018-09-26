package com.duyp.architecture.clean.android.powergit.data.entities.converters

import android.arch.persistence.room.TypeConverter
import java.text.ParseException
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return try {
            if (value == null) null else Date(value)
        } catch (e: ParseException) {
            null
        }

    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time ?: 0
    }
}