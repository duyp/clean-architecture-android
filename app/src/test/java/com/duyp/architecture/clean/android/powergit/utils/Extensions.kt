package com.duyp.architecture.clean.android.powergit.utils

import com.duyp.architecture.clean.android.powergit.Event

fun <T> Event<T>.noValue() = this.peekContent() == null

fun <T> Event<T>.content(expected: String) = this.peekContent() == expected
