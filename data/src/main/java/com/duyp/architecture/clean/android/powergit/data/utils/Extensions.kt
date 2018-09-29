package com.duyp.architecture.clean.android.powergit.data.utils

import android.content.SharedPreferences

fun SharedPreferences.putOrRemoveString(key: String, value: String?) {
    if (value == null)
        this.edit().remove(key).apply()
    else
        this.edit().putString(key, value).apply()
}