package com.duyp.architecture.clean.android.powergit.data.entities.converters

import androidx.room.TypeConverter
import java.text.ParseException
import java.util.*

object DateConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        try {
            return if (value == null) null else Date(value)
        } catch (e: ParseException) {
            return null
        }

    }

    @TypeConverter
    fun toString(value: Date?): Long? {
        return if (value == null) 0 else value.time
    }
}